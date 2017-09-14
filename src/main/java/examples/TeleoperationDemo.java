package examples;

import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.callback.TopicCallback;
import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.messages.geometry.Twist;
import ev3dev.rosbridge.publishers.EV3IRSensor;
import ev3dev.rosbridge.publishers.EV3UltrasonicSensor;
import ev3dev.rosbridge.subscribers.TeleOperation;
import lejos.hardware.port.SensorPort;

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

        //EV3IRSensor irSensor = new EV3IRSensor(ros, SensorPort.S1, "base_link");
        EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(ros, SensorPort.S2, "base_link");

        boolean flag = true;
        while (flag == true){
            //Empty
            //irSensor.publish();
            ultrasonicSensor.publish();
        }

        ros.disconnect();
    }

}