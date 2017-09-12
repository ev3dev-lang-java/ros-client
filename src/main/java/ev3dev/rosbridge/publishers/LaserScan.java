package ev3dev.rosbridge.publishers;

import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.messages.std.Header;
import edu.wpi.rail.jrosbridge.primitives.Time;
import ev3dev.sensors.slamtec.RPLidarA1;
import ev3dev.sensors.slamtec.RPLidarA1ServiceException;
import ev3dev.sensors.slamtec.RPLidarProviderListener;
import ev3dev.sensors.slamtec.model.Scan;
import ev3dev.sensors.slamtec.model.ScanDistance;
import org.slf4j.Logger;

public class LaserScan {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(LaserScan.class);

    private final Ros ros;
    private final String topicName;
    private final String DEFAULT_TOPIC_NAME = "scan";
    private final String dataType = edu.wpi.rail.jrosbridge.messages.sensor_msgs.LaserScan.TYPE;

    private final String sensorPort;
    private final String frameId;
    final RPLidarA1 lidar;
    private Time previousTime;
    private final Topic topic;
    private int counter_seq = 0;

    public LaserScan(
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
                previousTime = end_scan_time;

                log.trace("Scan time: {}, Samples: {}", scan_time, scan.getDistances().size());

                float angle_min = (float) Math.PI - convertDegreesToRadians(0.0f);
                float angle_max = (float) Math.PI - convertDegreesToRadians(359.0f);

                int node_count = 360;

                float angleIncrement = (angle_max - angle_min) / 360f;
                float timeIncrement = scan_time / node_count;

                //TODO Add in the future: inverted & angle_compensate

                final float[] ranges = new float[360];
                final float[] intensities = new float[0];

                for (ScanDistance distance: scan.getDistances()) {
                    if(distance.getAngle() >= 360){
                        log.warn("Warning: {}", distance.getAngle());
                        continue;
                    }
                    if(distance.getDistance() == 0.0f) {
                        ranges[(int)distance.getAngle()] = Float.MAX_VALUE;
                    }else {
                        ranges[(int)distance.getAngle()] = distance.getDistance()/100;
                    }
                }

                final Header header = new Header(counter_seq, Time.now(), frameId);
                final Message message = new edu.wpi.rail.jrosbridge.messages.sensor_msgs.LaserScan(
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

    public void close() throws RPLidarA1ServiceException {
        lidar.close();
    }

    private float convertDegreesToRadians(final float angle){
        return (float) (angle * Math.PI)/180;
    }

}
