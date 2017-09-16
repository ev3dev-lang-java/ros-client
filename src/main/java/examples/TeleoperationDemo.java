package examples;

import edu.wpi.rail.jrosbridge.Ros;
import ev3dev.rosbridge.publishers.EV3UltrasonicRange;
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

        //EV3IRRange irSensor = new EV3IRRange(ros, SensorPort.S1, "base_link");
        EV3UltrasonicRange ultrasonicSensor = new EV3UltrasonicRange(ros, SensorPort.S2, "base_link");

        boolean flag = true;
        while (flag == true){
            //Empty
            //irSensor.publish();
            ultrasonicSensor.publish();
        }

        ros.disconnect();
    }

}