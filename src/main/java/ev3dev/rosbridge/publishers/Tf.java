package ev3dev.rosbridge.publishers;

import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.messages.geometry.*;
import edu.wpi.rail.jrosbridge.messages.std.Header;
import edu.wpi.rail.jrosbridge.messages.tf.*;
import edu.wpi.rail.jrosbridge.primitives.Time;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Pose;

public class Tf {

    private final Ros ros;
    private final DifferentialPilot pilot;
    private final OdometryPoseProvider odometryPoseProvider;
    private final String frameId;
    private final String childFrameId;

    private final String topicName;
    private final String DEFAULT_TOPIC_NAME = "tf";
    private final String dataType = tfMessage.TYPE;

    private final Topic topic;

    private int counter_seq = 0;

    public Tf(
            final Ros ros,
            final DifferentialPilot pilot,
            final String frameId,
            final String childFrameId){
        this.ros = ros;
        this.pilot = pilot;
        this.odometryPoseProvider = new OdometryPoseProvider(pilot);
        this.frameId = frameId;
        this.childFrameId = childFrameId;

        topicName = DEFAULT_TOPIC_NAME;
        topic = new Topic(this.ros, this.topicName, dataType);

    }

    public void publish() {

        final Pose pose = odometryPoseProvider.getPose();

        TransformStamped[] transformStampedList = new TransformStamped[1];

        final Header header = new Header(counter_seq, Time.now(), frameId);
        Quaternion quaternion = getQuaternion(pose);
        Vector3 vector3 =  getTraslationVector(pose);
        Transform transform = new Transform(vector3, quaternion);
        TransformStamped transformStamped = new TransformStamped(header, childFrameId, transform );

        transformStampedList[0] = transformStamped;

        final Message message = new tfMessage(transformStampedList);
        topic.publish(message);

    }

    private Quaternion getQuaternion(final Pose pose){

        double attitude = Math.toRadians(pose.getHeading());
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

        //Quaternion
        double qw = c1c2*c3 - s1s2*s3;
        double qx = c1c2*s3 + s1s2*c3;
        double qy = s1*c2*c3 + c1*s2*s3;
        double qz = c1*s2*c3 - s1*c2*s3;

        return new Quaternion(qw, qx, qy, qz);
    }

    private Vector3 getTraslationVector(final Pose pose){

        double x = pose.getX() / 100;
        double y = pose.getY() /100;

        return new Vector3(x,y,0);
    }

}
