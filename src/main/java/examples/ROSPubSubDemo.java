package examples;

import edu.wpi.rail.jrosbridge.Ros;

import ev3dev.rosbridge.publishers.BrickBattery;
import ev3dev.rosbridge.publishers.Odom;
import ev3dev.rosbridge.publishers.Scan;
import ev3dev.rosbridge.subscribers.TeleOperation;
import ev3dev.sensors.Battery;
import ev3dev.sensors.slamtec.RPLidarA1ServiceException;
import lejos.robotics.navigation.DifferentialPilot;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by jabrena on 13/7/17.
 */
public @Slf4j class ROSPubSubDemo {

    //ROS Host
    private static final String rosAddress = "192.168.1.198";

    //LIDAR USB Port
    private static final String USBPort = "/dev/ttyUSB0";

    //TF Frames
    private static final String baseFrame = "base_link";
    private static final String odomFrame = "base_footprint";
    private static final String lidarFrame = "base_scan";

    //Pub
    private static BrickBattery energy;
    private static Scan laserScan;
    private static Odom odom;

    //Sub
    private static TeleOperation teleoperation;

    //Battery Threshold
    private static final float BATTERY_LIMIT = 7.6f;

    public static void main(String[] args) throws InterruptedException, RPLidarA1ServiceException {

        Battery battery = Battery.getInstance();

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

            float batteryLevel = battery.getVoltage();
            LOGGER.info("{}", batteryLevel);

            //Publish
            energy = new BrickBattery(ros, battery);
            laserScan = new Scan(ros, USBPort, lidarFrame);
            odom = new Odom(ros, pilot, baseFrame, odomFrame);

            //Subscribe
            teleoperation = new TeleOperation(ros, pilot);
            teleoperation.subscribe();

            while (batteryLevel > BATTERY_LIMIT) {
                batteryLevel = battery.getVoltage();

                energy.publish();
                laserScan.publish();
                odom.publish();
            }

            LOGGER.info("Robot with low battery level");
        }

        laserScan.close();
        ros.disconnect();
        System.exit(0);
    }



}
