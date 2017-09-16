package examples;

import edu.wpi.rail.jrosbridge.Ros;
import ev3dev.rosbridge.publishers.BrickBattery;
import ev3dev.rosbridge.publishers.Scan;
import ev3dev.sensors.slamtec.RPLidarA1ServiceException;

/**
 * Created by jabrena on 13/7/17.
 */
public class ROSBridgeTest {

    //ROS Host
    private static final String rosAddress = "192.168.1.70";

    //LIDAR USB Port
    private static final String USBPort = "/dev/ttyUSB0";

    //LIDAR ROS Frame
    private static final String lidarFrame = "base_scan";

    private static BrickBattery battery;
    private static Scan laserScan;

    public static void main(String[] args) throws InterruptedException, RPLidarA1ServiceException {

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("Close Lidar");
                try {
                    laserScan.close();
                } catch (RPLidarA1ServiceException e) {
                    e.printStackTrace();
                }
            }
        }));

        Ros ros = new Ros(rosAddress);
        ros.connect();
        System.out.println("ROSBridge Connected: " + ros.isConnected());

        battery = new BrickBattery(ros);
        laserScan = new Scan(ros, USBPort, lidarFrame);

        boolean flag = true;
        while (flag == true){
            battery.publish();
            laserScan.publish();
        }

        laserScan.close();
        ros.disconnect();
    }



}
