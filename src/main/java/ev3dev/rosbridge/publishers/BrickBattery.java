package ev3dev.rosbridge.publishers;

import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.messages.std.Float32;
import ev3dev.sensors.Battery;

public class BrickBattery {

    private final Ros ros;
    private final String topicName;
    private final String DEFAULT_TOPIC_NAME = "battery";
    private final String dataType = "std_msgs/Float32";

    private final Battery battery;

    public BrickBattery(final Ros ros) {
        this.ros = ros;
        topicName = DEFAULT_TOPIC_NAME;
        battery = Battery.getInstance();
    }

    public void publish(){

        final Topic topic = new Topic(this.ros, this.topicName, dataType);
        final Message message = new Float32(battery.getVoltage());
        topic.publish(message);
    }

}
