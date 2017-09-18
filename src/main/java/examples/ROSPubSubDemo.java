package examples;

import edu.wpi.rail.jrosbridge.Ros;
import ev3dev.rosbridge.publishers.*;
import ev3dev.rosbridge.subscribers.TeleOperation;
import ev3dev.sensors.slamtec.RPLidarA1ServiceException;
import lejos.hardware.port.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lombok.extern.slf4j.Slf4j;

import java.time.temporal.TemporalField;

/**
 * Created by jabrena on 13/7/17.
 */
public @Slf4j class ROSPubSubDemo {

    //ROS Host
    private static final String rosAddress = "192.168.1.198";

    //LIDAR USB Port
    private static final String USBPort = "/dev/ttyUSB0";

    //LIDAR ROS Frame
    private static final String lidarFrame = "base_scan";

    private static BrickBattery battery;
    private static Scan laserScan;
    private static Odom odom;
    private static Tf tf;
    private static TeleOperation teleoperation;

    public static void main(String[] args) throws InterruptedException, RPLidarA1ServiceException {

        PilotConfig pilotConfig = new PilotConfig();
        DifferentialPilot pilot = pilotConfig.getPilot();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                LOGGER.info("Close Lidar");
                try {
                    laserScan.close();
                    pilot.stop();
                } catch (RPLidarA1ServiceException e) {
                    e.printStackTrace();
                }
            }
        }));

        Ros ros = new Ros(rosAddress);
        ros.connect();
        LOGGER.info("ROSBridge Connected: " + ros.isConnected());

        if(ros.isConnected()) {

            //Publish
            battery = new BrickBattery(ros);
            laserScan = new Scan(ros, USBPort, lidarFrame);
            odom = new Odom(ros, pilot, "base_link", "base_footprint");
            tf = new Tf(ros, pilot, "base_link", "base_footprint");

            //Subscribe
            teleoperation = new TeleOperation(ros, pilot);
            teleoperation.subscribe();

            boolean flag = true;
            while (flag == true) {
                battery.publish();
                laserScan.publish();
                odom.publish();
                tf.publish();
            }

        }

        ros.disconnect();
    }



}
