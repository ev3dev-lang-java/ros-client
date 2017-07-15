package ev3dev.ros;

import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.messages.sensor_msgs.LaserScan;
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

    public LaserScanPublisher(
            final Ros ros,
            final String sensorPort,
            final String frameId) {

        this.ros = ros;
        topicName = DEFAULT_TOPIC_NAME;
        this.sensorPort = sensorPort;
        this.frameId = frameId;
        lidar = new RPLidarA1(sensorPort);
    }

    public void publish() throws RPLidarA1ServiceException {

        final Topic topic = new Topic(this.ros, this.topicName, dataType);

        lidar.init();
        lidar.addListener(new RPLidarProviderListener() {
            @Override
            public void scanFinished(Scan scan) {

                final Time start_scan_time = Time.now();//.fromMillis(System.currentTimeMillis());
                final Time end_scan_time = Time.now();//fromMillis(System.currentTimeMillis());

                float scan_time = (end_scan_time.subtract(start_scan_time).secs) * 1e-3f;

                float angle_min = convertDegreesToRadians(0.0f);
                float angle_max = convertDegreesToRadians(359.0f);

                int node_count = scan.getDistances().size();

                //message.setAngleMin((float) Math.PI - angle_min);
                //message.setAngleMax((float) Math.PI - angle_max);
                //message.setAngleIncrement(message.getAngleMax() - message.getAngleMin() / (node_count-1));
                //message.setScanTime(scan_time);
                //message.setTimeIncrement(scan_time / (node_count-1));

                //message.setRangeMin(0.15f);
                //message.setRangeMax(6);

                //TODO Add in the future: inverted & angle_compensate

                final float[] ranges = new float[scan.getDistances().size()];
                final float[] intensities = new float[scan.getDistances().size()];
                for (ScanDistance distance: scan.getDistances()) {

                    if (distance.getDistance() == 0.0){
                        ranges[distance.getAngle()] = Float.MAX_VALUE;
                    }else{
                        //TODO Upgrade library to return float
                        ranges[distance.getAngle()] = (float) distance.getDistance();
                    }

                    //TODO Review this value
                    //scan_msg.intensities[i] = (float) (nodes[i].sync_quality >> 2);
                    intensities[distance.getAngle()] = distance.getQuality();
                }

                //message.getHeader().setFrameId(frameId);
                //message.getHeader().setStamp(Time.fromMillis(System.currentTimeMillis()));
                //publisher.publish(message);

                //final Message message = //new Message("" + battery.getVoltage());
                final Message message = new LaserScan(
                        null,
                        angle_min,
                        angle_max,
                        0f,//angleIncrement,
                        0f,//timeIncrement,
                        0f,//timeScan,
                        0f,//rangeMin,
                        0f,//rangeMax,
                        ranges,
                        intensities
                );
                topic.publish(message);
            }
        });
    }

    private float convertDegreesToRadians(final float angle){
        return (float) (angle * Math.PI)/180;
    }

}
