package edu.wpi.rail.jrosbridge.messages.nav_msgs;

import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.messages.geometry.PoseWithCovariance;
import edu.wpi.rail.jrosbridge.messages.geometry.TwistWithCovariance;
import edu.wpi.rail.jrosbridge.messages.std.Header;

import javax.json.Json;

/**
 * Created by jabrena on 15/9/17.
 */
public class Odometry extends Message{

    public static final String TYPE = "nav_msgs/Odometry";
    public static final String DEFINITION =
            "# This represents an estimate of a position and velocity in free space.  \n" +
            "# The pose in this message should be specified in the coordinate frame given by header.frame_id.\n" +
            "# The twist in this message should be specified in the coordinate frame given by the child_frame_id\n" +
            "Header header\n" +
            "string child_frame_id\n" +
            "geometry_msgs/PoseWithCovariance pose\n" +
            "geometry_msgs/TwistWithCovariance twist\n";

    private final Header header;
    private final String childFrameId;
    private final PoseWithCovariance poseWithCovariance;
    private final TwistWithCovariance twistWithCovariance;

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_CHILD_FRAME_ID = "child_frame_id";
    public static final String FIELD_POSE = "pose";
    public static final String FIELD_TWIST = "twist";

    public Odometry(
            final Header header,
            final String childFrameId,
            final PoseWithCovariance poseWithCovariance,
            final TwistWithCovariance twistWithCovariance) {

        // build the JSON object
        super(Json
                .createObjectBuilder()
                .add(Odometry.FIELD_HEADER, header.toJsonObject())
                .add(Odometry.FIELD_CHILD_FRAME_ID, childFrameId)
                .add(Odometry.FIELD_POSE, poseWithCovariance.toJsonObject())
                .add(Odometry.FIELD_TWIST, twistWithCovariance.toJsonObject())
                .build(), Odometry.TYPE);

        this.header = header;
        this.childFrameId = childFrameId;
        this.poseWithCovariance = poseWithCovariance;
        this.twistWithCovariance = twistWithCovariance;
    }

    public Header getHeader() {
        return this.header;
    }

    public String getChildFrameId(){
        return this.childFrameId;
    }

    public PoseWithCovariance getPose(){
        return this.poseWithCovariance;
    }

    public TwistWithCovariance getTwist(){
        return this.twistWithCovariance;
    }

}
