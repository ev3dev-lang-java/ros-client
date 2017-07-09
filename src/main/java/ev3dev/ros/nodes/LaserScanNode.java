package ev3dev.ros.nodes;

import ev3dev.sensors.slamtec.RPLidarA1;
import ev3dev.sensors.slamtec.RPLidarA1ServiceException;
import ev3dev.sensors.slamtec.model.Scan;
import ev3dev.sensors.slamtec.model.ScanDistance;
import org.ros.concurrent.CancellableLoop;
import org.ros.message.Time;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import sensor_msgs.LaserScan;

public class LaserScanNode extends AbstractNodeMain {

    private final java.lang.String NODE_NAME = "laserScan";

    private final String sensorPort;
    private final String frameId;
    final RPLidarA1 lidar;

    public LaserScanNode(
            final String sensorPort,
            final String frameId)
            throws RPLidarA1ServiceException {
        this.sensorPort = sensorPort;
        this.frameId = frameId;
        lidar = new RPLidarA1(sensorPort);
        lidar.init();
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of(NODE_NAME);
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {

        final Publisher<LaserScan> publisher =
                connectedNode.newPublisher("scan", sensor_msgs.LaserScan._TYPE);

        connectedNode.executeCancellableLoop(new CancellableLoop() {

            @Override
            protected void setup() {

            }

            @Override
            protected void loop() throws InterruptedException {

                sensor_msgs.LaserScan message = publisher.newMessage();

                try {

                    final Time start_scan_time = Time.fromMillis(System.currentTimeMillis());
                    final Scan scan = lidar.scan();
                    final Time end_scan_time = Time.fromMillis(System.currentTimeMillis());

                    float scan_time = (end_scan_time.subtract(start_scan_time).secs) * 1e-3f;

                    float angle_min = convertDegreesToRadians(0.0f);
                    float angle_max = convertDegreesToRadians(359.0f);

                    int node_count = scan.getDistances().size();

                    message.setAngleMin((float) Math.PI - angle_min);
                    message.setAngleMax((float) Math.PI - angle_max);
                    message.setAngleIncrement(
                            message.getAngleMax() - message.getAngleMin() / (node_count-1));

                    message.setScanTime(scan_time);
                    message.setTimeIncrement(scan_time / (node_count-1));

                    message.setRangeMin(0.15f);
                    message.setRangeMax(6);

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

                    message.getHeader().setFrameId(frameId);
                    message.getHeader().setStamp(Time.fromMillis(System.currentTimeMillis()));
                    publisher.publish(message);

                } catch (RPLidarA1ServiceException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    private float convertDegreesToRadians(final float angle){
        return (float) (angle * Math.PI)/180;
    }
}
