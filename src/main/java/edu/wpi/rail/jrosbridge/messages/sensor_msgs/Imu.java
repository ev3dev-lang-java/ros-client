package edu.wpi.rail.jrosbridge.messages.sensor_msgs;

//import com.sun.rowset.internal.Row;
import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.messages.geometry.Quaternion;
import edu.wpi.rail.jrosbridge.messages.geometry.Vector3;
import edu.wpi.rail.jrosbridge.messages.std.Header;

import javax.json.Json;
import java.io.StringReader;
import java.util.Arrays;

/**
 * Created by jabrena on 14/8/17.
 */
public class Imu extends Message {

    public static final String TYPE = "sensor_msgs/Imu";
    public static String DEFINITION =
            "# This is a message to hold data from an IMU (Inertial Measurement Unit)\n#" +
            "# Accelerations should be in m/s^2 (not in g\'s), and rotational velocity should be in rad/sec\n#" +
            "# If the covariance of the measurement is known, it should be filled in (if all you know is the \n" +
            " # variance of each measurement, e.g. from the datasheet, just put those along the diagonal)\n" +
            "# A covariance matrix of all zeros will be interpreted as \"covariance unknown\", and to use the\n" +
            "# data a covariance will have to be assumed or gotten from some other source\n#" +
            "# If you have no estimate for one of the data elements (e.g. your IMU doesn\'t produce an orientation \n" +
            "# estimate), please set element 0 of the associated covariance matrix to -1\n" +
            "# If you are interpreting this message, please check for a value of -1 in the first element of each \n" +
            "# covariance matrix, and disregard the associated estimate.\n\n" +
            "Header header\n\n" +
            "geometry_msgs/Quaternion orientation\n" +
            "float64[9] orientation_covariance" +
            "# Row major about x, y, z axes\n\n" +
            "geometry_msgs/Vector3 angular_velocity\n" +
            "float64[9] angular_velocity_covariance" +
            "# Row major about x, y, z axes\n\n" +
            "geometry_msgs/Vector3 linear_acceleration\n" +
            "float64[9] linear_acceleration_covariance" +
            "# Row major x, y z \n";

    private final Header header;

    private final Quaternion orientation;
    private final float[] orientation_covariance; //# Row major about x, y, z axes

    private final Vector3 angular_velocity;
    private final float[] angular_velocity_covariance; //# Row major about x, y, z axes

    private final Vector3 linear_acceleration;
    private final float[] linear_acceleration_covariance; //# Row major x, y z

    private static final String FIELD_HEADER = "header";
    private static final String FIELD_ORIENTATION = "orientation";
    private static final String FIELD_ORIENTATION_COVARIANCE = "orientation_covariance";
    private static final String FIELD_ANGULAR_VELOCITY = "angular_velocity";
    private static final String FIELD_ANGULAR_VELOCITY_COVARIANCE = "angular_velocity_covariance";
    private static final String FIELD_LINEAR_ACCELERATION = "linear_acceleration";
    private static final String FIELD_LINEAR_ACCELERATION_COVARIANCE = "linear_acceleration_covariance";

    public Imu(
            final Header header,
            final Quaternion orientation,
            final float[] orientation_covariance,
            final Vector3 angular_velocity,
            final float[] angular_velocity_covariance,
            final Vector3 linear_acceleration,
            final float[] linear_acceleration_covariance) {

        // build the JSON object
        super(Json
                .createObjectBuilder()
                .add(Imu.FIELD_HEADER, header.toJsonObject())
                .add(Imu.FIELD_ORIENTATION, orientation.toJsonObject())
                .add(Imu.FIELD_ORIENTATION_COVARIANCE,
                        Json.createReader(
                                new StringReader(Arrays.toString(orientation_covariance))).readArray())
                .add(Imu.FIELD_ANGULAR_VELOCITY, angular_velocity.toJsonObject())
                .add(Imu.FIELD_ANGULAR_VELOCITY_COVARIANCE,
                        Json.createReader(
                                new StringReader(Arrays.toString(orientation_covariance))).readArray())
                .add(Imu.FIELD_LINEAR_ACCELERATION, linear_acceleration.toJsonObject())
                .add(Imu.FIELD_LINEAR_ACCELERATION_COVARIANCE,
                        Json.createReader(
                                new StringReader(Arrays.toString(orientation_covariance))).readArray())
                .build(), Imu.TYPE);

        this.header = header;
        this.orientation = orientation;
        this.orientation_covariance = orientation_covariance;
        this.angular_velocity = angular_velocity;
        this.angular_velocity_covariance = angular_velocity_covariance;
        this.linear_acceleration = linear_acceleration;
        this.linear_acceleration_covariance = linear_acceleration_covariance;
    }

    public Header getHeader() {
        return header;
    }

    public Quaternion getOrientation() {
        return orientation;
    }

    public float[] getOrientation_covariance() {
        return orientation_covariance;
    }

    public Vector3 getAngular_velocity() {
        return angular_velocity;
    }

    public float[] getAngular_velocity_covariance() {
        return angular_velocity_covariance;
    }

    public Vector3 getLinear_acceleration() {
        return linear_acceleration;
    }

    public float[] getLinear_acceleration_covariance() {
        return linear_acceleration_covariance;
    }

    /**
     * Create a clone of this LaserScan.
     */
    @Override
    public Imu clone() {
        return new Imu(
            this.header,
            this.orientation,
            this.orientation_covariance,
            this.angular_velocity,
            this.angular_velocity_covariance,
            this.linear_acceleration,
            this.linear_acceleration_covariance);
    }

}
