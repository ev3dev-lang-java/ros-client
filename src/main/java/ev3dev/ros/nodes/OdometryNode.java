package ev3dev.ros.nodes;

import geometry_msgs.PoseStamped;
import geometry_msgs.Quaternion;
import geometry_msgs.TransformStamped;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import nav_msgs.Odometry;
import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import tf2_msgs.TFMessage;

public class OdometryNode extends AbstractNodeMain {

    private final String NODE_NAME = "odometry";

    private final PoseProvider poseProvider;
    private final double linearSpeed;
    private final double angularSpeed;
    private final String frameId;
    
    public OdometryNode(
            final Navigator navigator,
            final String frameId) {
        this.poseProvider = navigator.getPoseProvider();
        linearSpeed = navigator.getMoveController().getLinearSpeed();
        angularSpeed = 0;//navigator.getMoveController().getAngularSpeed();
        this.frameId = frameId;
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of(NODE_NAME);
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {

        connectedNode.executeCancellableLoop(new CancellableLoop() {

            @Override
            protected void setup() {

            }

            @Override
            protected void loop() throws InterruptedException {
                poseToTransform(connectedNode, poseProvider.getPose(), angularSpeed, linearSpeed);
            }

        });

    }

    /*
    * Convert a leJOS pose into a world to robot transform
    */
    public void poseToTransform(ConnectedNode node, Pose p, double angularVelocity, double linearVelocity) {

        String tfMessageType = "tf/tfMessage";
        Publisher<TFMessage> tfTopic;

        String odomMessageType = "nav_msgs/Odometry";
        Publisher<Odometry> odomTopic;

        String poseMessageType = "geometry_msgs/PoseStamped";
        Publisher<PoseStamped> poseTopic;

        final String WORLD_FRAME = "/world";
        final String ROBOT_FRAME = "/robot";

        TFMessage tf = node.getTopicMessageFactory().newFromType(TFMessage._TYPE);
        TransformStamped tr = node.getTopicMessageFactory().newFromType(TransformStamped._TYPE);
        Odometry od = node.getTopicMessageFactory().newFromType(Odometry._TYPE);
        PoseStamped poseStamped = node.getTopicMessageFactory().newFromType(PoseStamped._TYPE);

        tfTopic = node.newPublisher("tf", tfMessageType);
        odomTopic = node.newPublisher("odom", odomMessageType);
        poseTopic = node.newPublisher("pose", poseMessageType);

        double attitude = Math.toRadians(p.getHeading()); // Why attitude, not heading?
        double bank = 0;
        double heading = 0;
        double c1 = Math.cos(heading/2);
        double s1 = Math.sin(heading/2);
        double c2 = Math.cos(attitude/2);
        double s2 = Math.sin(attitude/2);
        double c3 = Math.cos(bank/2);
        double s3 = Math.sin(bank/2);
        double c1c2 = c1*c2;
        double s1s2 = s1*s2;

        System.out.println("Angular: " + angularVelocity + ", linear: " + linearVelocity);

        Quaternion q = node.getTopicMessageFactory().newFromType(Quaternion._TYPE);
        q.setW(c1c2*c3 - s1s2*s3);
        q.setX(c1c2*s3 + s1s2*c3);
        q.setY(s1*c2*c3 + c1*s2*s3);
        q.setZ(c1*s2*c3 - s1*c2*s3);

        double x = p.getX() / 100;
        double y = p.getY() /100;


        tf = node.getTopicMessageFactory().newFromType(TFMessage._TYPE);

        // transform for robot in the world
        tr.getHeader().setFrameId(WORLD_FRAME);
        tr.getHeader().setStamp(node.getCurrentTime());
        tr.setChildFrameId(ROBOT_FRAME);

        tr.getTransform().getTranslation().setX(x);
        tr.getTransform().getTranslation().setY(y);
        tr.getTransform().getTranslation().setZ(0);

        tr.getTransform().setRotation(q);

        java.util.List<TransformStamped> trs = tf.getTransforms();
        trs.add(tr);

        od.getHeader().setStamp(node.getCurrentTime());
        od.getHeader().setFrameId(WORLD_FRAME);
        od.setChildFrameId(ROBOT_FRAME);

        od.getPose().getPose().getPosition().setX(x);
        od.getPose().getPose().getPosition().setY(y);
        od.getPose().getPose().getPosition().setZ(0);

        od.getPose().getPose().setOrientation(q);

        double[] cov = { // Copied from turtle bot
	    		/* 1e-3,*/ 0, 0, 0, 0, 0,
                0, 1e-3, 0, 0, 0, 0,
                0, 0, 1e6, 0, 0, 0,
                0, 0, 0, 1e6, 0, 0,
                0, 0, 0, 0, 1e6, 0,
                0, 0, 0, 0, 0, 1e3, 0};

        //od.getPose().setCovariance(cov);

        //od.getPose().getCovariance()[0] = 1e-3;

        //System.out.println("Setting covariance to " + cov);
        od.getTwist().getTwist().getLinear().setX(linearVelocity);
        od.getTwist().getTwist().getLinear().setY(0);
        od.getTwist().getTwist().getLinear().setZ(0);

        od.getTwist().getTwist().getAngular().setX(0);
        od.getTwist().getTwist().getAngular().setY(0);
        od.getTwist().getTwist().getAngular().setZ(angularVelocity);

        //od.getTwist().setCovariance(cov);

        poseStamped.getHeader().setStamp(node.getCurrentTime());
        poseStamped.getHeader().setFrameId(WORLD_FRAME);

        poseStamped.getPose().getPosition().setX(x);
        poseStamped.getPose().getPosition().setY(y);
        poseStamped.getPose().getPosition().setZ(0);
        poseStamped.getPose().setOrientation(q);

        tfTopic.publish(tf);
        odomTopic.publish(od);
        poseTopic.publish(poseStamped);
    }
}
