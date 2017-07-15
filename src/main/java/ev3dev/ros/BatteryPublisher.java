package ev3dev.ros;

import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.messages.Message;
import ev3dev.sensors.Battery;

public class BatteryPublisher {

    private final Ros ros;
    private final String topicName;
    private final String DEFAULT_TOPIC_NAME = "battery";
    private final String dataType = "std_msgs/Float32";

    private final Battery battery;

    public BatteryPublisher(final Ros ros) {
        this.ros = ros;
        topicName = DEFAULT_TOPIC_NAME;
        battery = Battery.getInstance();
    }

    public void publish(){

        final Topic topic = new Topic(this.ros, this.topicName, dataType);
        final Message message = new Message("" + battery.getVoltage());
        topic.publish(message);
    }

}
