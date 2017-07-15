package examples;

import edu.wpi.rail.jrosbridge.Ros;
import ev3dev.ros.BatteryPublisher;

/**
 * Created by jabrena on 13/7/17.
 */
public class ROSBridgeTest {

    private static final String rosAddress = "192.168.1.70";

    private static BatteryPublisher battery;

    public static void main(String[] args) throws InterruptedException {

        Ros ros = new Ros(rosAddress);
        ros.connect();

        battery = new BatteryPublisher(ros);

        boolean flag = true;
        while (flag == true){
            battery.publish();
        }

        ros.disconnect();
    }

}
