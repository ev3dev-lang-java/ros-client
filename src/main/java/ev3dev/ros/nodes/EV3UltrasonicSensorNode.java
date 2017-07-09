package ev3dev.ros.nodes;

import ev3dev.sensors.ev3.EV3IRSensor;
import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.hardware.port.Port;
import lejos.robotics.SampleProvider;
import org.ros.concurrent.CancellableLoop;
import org.ros.message.Time;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import sensor_msgs.Range;

public class EV3UltrasonicSensorNode extends AbstractNodeMain {

    private final String NODE_NAME = "range";

    private final Port sensorPort;
    private final String frameId;

    public EV3UltrasonicSensorNode(
            final Port sensorPort,
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

        final EV3UltrasonicSensor irSensor = new EV3UltrasonicSensor(sensorPort);
        final SampleProvider sampleProvider = irSensor.getDistanceMode();
        final Publisher<Range> publisher = connectedNode.newPublisher("range", Range._TYPE);

        connectedNode.executeCancellableLoop(new CancellableLoop() {

            @Override
            protected void setup() {

            }

            @Override
            protected void loop() throws InterruptedException {
                Range message = publisher.newMessage();

                //Node configuration
                message.setMinRange(0.05f);
                message.setMaxRange(2.5f);
                message.setRadiationType((byte) 0); //Ultrasonic
                message.setFieldOfView(0.5f); // radian

                float [] sample = new float[sampleProvider.sampleSize()];
                sampleProvider.fetchSample(sample, 0);
                message.setRange(sample[0]);

                message.getHeader().setFrameId(frameId);
                message.getHeader().setStamp(Time.fromMillis(System.currentTimeMillis()));
                publisher.publish(message);
            }

        });
    }
}
