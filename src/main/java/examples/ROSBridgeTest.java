package examples;

import edu.wpi.rail.jrosbridge.Ros;
import ev3dev.ros.BatteryPublisher;
import ev3dev.ros.LaserScanPublisher;
import ev3dev.sensors.slamtec.RPLidarA1ServiceException;

/**
 * Created by jabrena on 13/7/17.
 */
public class ROSBridgeTest {

    private static final String rosAddress = "192.168.1.70";

    private static BatteryPublisher battery;
    private static final String USBPort = "/dev/ttyUSB0";
    private static LaserScanPublisher laserScan;

    public static void main(String[] args) throws InterruptedException, RPLidarA1ServiceException {

        Ros ros = new Ros(rosAddress);
        ros.connect();

        battery = new BatteryPublisher(ros);
        laserScan = new LaserScanPublisher(ros,USBPort,"base");

        boolean flag = true;
        while (flag == true){
            battery.publish();
            laserScan.publish();
        }

        ros.disconnect();
    }

}
