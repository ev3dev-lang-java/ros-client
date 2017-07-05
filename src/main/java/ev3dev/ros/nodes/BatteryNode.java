package ev3dev.ros.nodes;

import ev3dev.sensors.Battery;
import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import std_msgs.Float32;

public class BatteryNode extends AbstractNodeMain {

    private final java.lang.String NODE_NAME = "BrickBattery";

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of(NODE_NAME);
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {

        final Battery battery = Battery.getInstance();
        final Publisher<Float32> publisher = connectedNode.newPublisher("battery", Float32._TYPE);

        connectedNode.executeCancellableLoop(new CancellableLoop() {

            @Override
            protected void setup() {

            }

            @Override
            protected void loop() throws InterruptedException {
                Float32 scan = publisher.newMessage();
                scan.setData(battery.getVoltage());
                publisher.publish(scan);
                Thread.sleep(1000);
            }

        });
    }
}
