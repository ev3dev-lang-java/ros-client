package examples;

import ev3dev.sensors.Battery;
import ev3dev.sensors.arduino.bn055.BNO055;
import ev3dev.sensors.arduino.bn055.BNO055Listener;
import ev3dev.sensors.arduino.bn055.model.BNO055Response;
import ev3dev.sensors.slamtec.RPLidarA1;
import ev3dev.sensors.slamtec.RPLidarA1ServiceException;
import ev3dev.sensors.slamtec.RPLidarProviderListener;
import ev3dev.sensors.slamtec.model.Scan;
import lombok.extern.slf4j.Slf4j;

public @Slf4j class CombinedUSBDevices {

    public static void main(String[] args) throws Exception {

        LOGGER.info("Testing RPLidar/BNO055 on an EV3Dev Brick with Java");

        final String USBPorts[] = {
                "/dev/ttyUSB0",
                "/dev/ttyACM0"
        };

        final RPLidarA1 lidar = new RPLidarA1(USBPorts[0]);
        final BNO055 bno055 = new BNO055(USBPorts[1]);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                LOGGER.info("Closing Lidar & Arduino connection");
                try {
                    lidar.close();
                    bno055.close();
                } catch (RPLidarA1ServiceException e) {
                    e.printStackTrace();
                }
            }
        }));

        lidar.init();
        bno055.init();

        lidar.addListener(new RPLidarProviderListener() {

            @Override
            public void scanFinished(final Scan scan) {
                LOGGER.info("Angles: {}", scan.getDistances().size());
            }
        });

        bno055.addListener(new BNO055Listener() {

            @Override
            public void dataReceived(BNO055Response response) {
                LOGGER.info("Sample: {}", response);
            }
        });

        int counter = 0;

        boolean flag = true;
        while(flag){

            lidar.scan();

            counter++;
            LOGGER.info("Counter: {}", counter);

            if(counter > 5){
                break;
            }
        }

        LOGGER.info("{}", Battery.getInstance().getVoltage());
        lidar.close();
        LOGGER.info("End");
        System.exit(0);
    }

}
