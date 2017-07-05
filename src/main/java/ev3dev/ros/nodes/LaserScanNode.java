package ev3dev.ros.nodes;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import sensor_msgs.LaserScan;

public class LaserScanNode extends AbstractNodeMain {

    private final java.lang.String NODE_NAME = "BrickLaserScan";

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of(NODE_NAME);
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {

        final Publisher<LaserScan> publisher =
                connectedNode.newPublisher("scan", sensor_msgs.LaserScan._TYPE);

        connectedNode.executeCancellableLoop(new CancellableLoop() {

            @Override
            protected void setup() {

            }

            @Override
            protected void loop() throws InterruptedException {
                sensor_msgs.LaserScan scan = publisher.newMessage();

                publisher.publish(scan);
            }

        });
    }
}
