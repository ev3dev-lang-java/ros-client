package ev3dev.ros.nodes;

import ev3dev.sensors.slamtec.RPLidarA1;
import org.ros.concurrent.CancellableLoop;
import org.ros.message.Time;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import sensor_msgs.LaserScan;

public class LaserScanNode extends AbstractNodeMain {

    private final java.lang.String NODE_NAME = "laserScan";

    private final String sensorPort;
    private final String frameId;

    public LaserScanNode(
            final String sensorPort,
            final String frameId) {
        this.sensorPort = sensorPort;
        this.frameId = frameId;
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of(NODE_NAME);
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {

        final RPLidarA1 lidar = new RPLidarA1(sensorPort);
        final Publisher<LaserScan> publisher =
                connectedNode.newPublisher("scan", sensor_msgs.LaserScan._TYPE);

        connectedNode.executeCancellableLoop(new CancellableLoop() {

            @Override
            protected void setup() {

            }

            @Override
            protected void loop() throws InterruptedException {

                sensor_msgs.LaserScan message = publisher.newMessage();



                message.getHeader().setFrameId(frameId);
                message.getHeader().setStamp(Time.fromMillis(System.currentTimeMillis()));
                publisher.publish(message);
            }

        });
    }
}
