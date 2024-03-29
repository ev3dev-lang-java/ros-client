package ev3dev.rosbridge.publishers;

import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.messages.sensor_msgs.Range;
import edu.wpi.rail.jrosbridge.messages.std.Header;
import edu.wpi.rail.jrosbridge.primitives.Time;
import ev3dev.sensors.ev3.EV3IRSensor;
import lejos.hardware.port.Port;
import lejos.robotics.SampleProvider;
import lombok.extern.slf4j.Slf4j;

public @Slf4j class EV3IRRange {

    private final Ros ros;
    private final String topicName;
    private final String DEFAULT_TOPIC_NAME = "range";

    private final Port sensorPort;
    private final String frameId;

    private EV3IRSensor irSensor;
    private SampleProvider sampleProvider;
    private int sampleSize;


    public EV3IRRange(
            final Ros ros,
            final Port sensorPort,
            final String frameId) {

        LOGGER.info("Starting an EV3 IR Sensor publisher");

        this.ros = ros;
        this.sensorPort = sensorPort;
        this.frameId = frameId;
        this.topicName = DEFAULT_TOPIC_NAME;

        this.init();
    }

    private void init(){
        irSensor = new EV3IRSensor(sensorPort);
        sampleProvider = irSensor.getDistanceMode();
        sampleSize = sampleProvider.sampleSize();
    }

    private int counter_seq = 0;

    public void publish(){

        float [] sample = new float[sampleSize];
        sampleProvider.fetchSample(sample, 0);
        float distance = sample[0] / 100;

        final float minRange = EV3IRSensor.MIN_RANGE / 100f;
        final float maxRange = EV3IRSensor.MAX_RANGE / 100f;

        if(distance != Float.POSITIVE_INFINITY) {
            final Topic topic = new Topic(this.ros, this.topicName, Range.TYPE);
            final Header header = new Header(counter_seq, Time.now(), frameId);
            final Message message = new Range(
                    header,
                    Range.INFRARED,
                    0.5f,
                    minRange,
                    maxRange,
                    distance
            );
            topic.publish(message);
        }
    }
}
