package examples;

import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.callback.TopicCallback;
import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.messages.geometry.Twist;
import ev3dev.rosbridge.subscribers.TeleOperation;

/**
 * Created by jabrena on 13/7/17.
 */
public class TeleoperationDemo {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Running a TeleOperation Demo");

        Ros ros = new Ros("192.168.1.198");
        ros.connect();

        System.out.println("Connected");

        PilotConfig pilotConfig = new PilotConfig();

        TeleOperation teleoperation = new TeleOperation(ros, pilotConfig.getPilot());
        teleoperation.subscribe();

        boolean flag = true;
        while (flag == true){
            //Empty
        }

        ros.disconnect();
    }

}