package ev3dev.rosbridge.publishers;

import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.primitives.Time;
import ev3dev.sensors.arduino.bn055.BNO055;
import org.slf4j.Logger;

/**
 * Created by jabrena on 13/8/17.
 */
public class IMU {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Scan.class);

    private final Ros ros;
    private final String topicName;
    private final String DEFAULT_TOPIC_NAME = "imu";
    private final String dataType = null;//Imu.TYPE;

    private final String sensorPort;
    private final String frameId;
    final BNO055 bno055;
    private Time previousTime;
    private final Topic topic;
    private int counter_seq = 0;

    public IMU(
            final Ros ros,
            final String sensorPort,
            final String frameId) {

        this.ros = ros;
        topicName = DEFAULT_TOPIC_NAME;
        this.sensorPort = sensorPort;
        this.frameId = frameId;
        bno055 = new BNO055(sensorPort);

        topic = new Topic(this.ros, this.topicName, dataType);

    }


}
