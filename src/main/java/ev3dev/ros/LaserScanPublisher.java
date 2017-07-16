package ev3dev.ros;

import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.messages.sensor_msgs.LaserScan;
import edu.wpi.rail.jrosbridge.messages.std.Header;
import edu.wpi.rail.jrosbridge.primitives.Time;
import ev3dev.sensors.slamtec.RPLidarA1;
import ev3dev.sensors.slamtec.RPLidarA1ServiceException;
import ev3dev.sensors.slamtec.RPLidarProviderListener;
import ev3dev.sensors.slamtec.model.Scan;
import ev3dev.sensors.slamtec.model.ScanDistance;

public class LaserScanPublisher {

    private final Ros ros;
    private final String topicName;
    private final String DEFAULT_TOPIC_NAME = "scan";
    private final String dataType = LaserScan.TYPE;

    private final String sensorPort;
    private final String frameId;
    final RPLidarA1 lidar;
    private Time previousTime;
    private final Topic topic;
    private int counter_seq = 0;

    public LaserScanPublisher(
            final Ros ros,
            final String sensorPort,
            final String frameId) throws RPLidarA1ServiceException {

        this.ros = ros;
        topicName = DEFAULT_TOPIC_NAME;
        this.sensorPort = sensorPort;
        this.frameId = frameId;
        lidar = new RPLidarA1(sensorPort);
        previousTime = Time.now();
        lidar.init();

        topic = new Topic(this.ros, this.topicName, dataType);

        lidar.addListener(new RPLidarProviderListener() {

            @Override
            public void scanFinished(Scan scan) {

                final Time end_scan_time = Time.now();
                float scan_time = (end_scan_time.subtract(previousTime).secs) * 1e-3f;

                System.out.println(scan_time);

                float angle_min = (float) Math.PI - convertDegreesToRadians(0.0f);
                float angle_max = (float) Math.PI - convertDegreesToRadians(359.0f);

                int node_count = 360;//scan.getDistances().size();

                //float angleIncrement = (angle_max - angle_min) / node_count;
                float angleIncrement = (angle_max - angle_min) / 360;
                float timeIncrement = scan_time / node_count;

                System.out.println("demo" + timeIncrement);

                //message.setScanTime(scan_time);
                //message.setTimeIncrement(scan_time / (node_count-1));

                //message.setRangeMin(0.15f);
                //message.setRangeMax(6);

                //TODO Add in the future: inverted & angle_compensate

                final float[] ranges = new float[360];
                final float[] intensities = new float[0];

                for (ScanDistance distance: scan.getDistances()) {
                    if(distance.getDistance() == 0.0) {
                        ranges[distance.getAngle()] = Float.MAX_VALUE;
                    }else {
                        if(distance.getAngle() > 360){
                            System.out.println("Warning: " + distance.getAngle());
                            continue;
                        }
                        ranges[distance.getAngle()] = (float) distance.getDistance()/100;
                    }
                }

                //message.getHeader().setFrameId(frameId);

                Header header = new Header(counter_seq, Time.now(), "map");

                final Message message = new LaserScan(
                        header,
                        angle_min,
                        angle_max,
                        angleIncrement,
                        timeIncrement,
                        scan_time,
                        0.15f,
                        6f,
                        ranges,
                        intensities
                );

                topic.publish(message);

            }
        });
    }

    public void publish() throws RPLidarA1ServiceException {

        lidar.scan();
    }

    private float convertDegreesToRadians(final float angle){
        return (float) (angle * Math.PI)/180;
    }

}
