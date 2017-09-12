package edu.wpi.rail.jrosbridge.messages.sensor_msgs;

import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.messages.std.Header;

import javax.json.Json;
import java.io.StringReader;
import java.util.Arrays;

public class LaserScan extends Message {

    public static final String TYPE = "sensor_msgs/LaserScan";
    public static final String DEFINITION =
            "# Single scan from a planar laser range-finder\n#\n" +
            "# If you have another ranging device with different behavior (e.g. a sonar\n" +
            "# array), please find or create a different message, since applications\n" +
            "# will make fairly laser-specific assumptions about this data\n\n" +
            "Header header            " +
            "# timestamp in the header is the acquisition time of \n                         " +
            "# the first ray in the scan.\n                         #\n                         " +
            "# in frame frame_id, angles are measured around \n                         " +
            "# the positive Z axis (counterclockwise, if Z is up)\n                         " +
            "# with zero angle being forward along the x axis\n                         \nfloat32 angle_min        " +
            "# start angle of the scan [rad]\nfloat32 angle_max        " +
            "# end angle of the scan [rad]\nfloat32 angle_increment  " +
            "# angular distance between measurements [rad]\n\nfloat32 time_increment   " +
            "# time between measurements [seconds] - if your scanner\n                         " +
            "# is moving, this will be used in interpolating position\n                         " +
            "# of 3d points\nfloat32 scan_time        " +
            "# time between scans [seconds]\n\nfloat32 range_min        " +
            "# minimum range value [m]\nfloat32 range_max        " +
            "# maximum range value [m]\n\nfloat32[] ranges         " +
            "# range data [m] (Note: values < range_min or > range_max should be discarded)\nfloat32[] intensities    " +
            "# intensity data [device-specific units].  If your\n                         " +
            "# device does not provide intensities, please leave\n                         " +
            "# the array empty.\n";

    private final Header header;
    private final float angleMin;
    private final float angleMax;
    private final float angleIncrement;
    private final float timeIncrement;
    private final float timeScan;
    private final float rangeMin;
    private final float rangeMax;
    private final float[] ranges;
    private final float[] intensities;

    private static final String FIELD_HEADER = "header";
    private static final String FIELD_ANGLE_MIN = "angle_min";
    private static final String FIELD_ANGLE_MAX = "angle_max";
    private static final String FIELD_ANGLE_INCREMENT = "angle_increment";
    private static final String FIELD_TIME_INCREMENT = "time_increment";
    private static final String FIELD_TIME_SCAN = "scan_time";
    private static final String FIELD_RANGE_MIN = "range_min";
    private static final String FIELD_RANGE_MAX = "range_max";
    private static final String FIELD_RANGES = "ranges";
    private static final String FIELD_INTENSITIES = "intensities";

    public LaserScan(
            Header header,
            final float angleMin,
            final float angleMax,
            final float angleIncrement,
            final float timeIncrement,
            final float timeScan,
            final float rangeMin,
            final float rangeMax,
            final float[] ranges,
            final float[] intensities){

        // build the JSON object
        super(Json
            .createObjectBuilder()
            .add(LaserScan.FIELD_HEADER, header.toJsonObject())
            .add(LaserScan.FIELD_ANGLE_MIN, angleMin)
            .add(LaserScan.FIELD_ANGLE_MAX, angleMax)
            .add(LaserScan.FIELD_ANGLE_INCREMENT, angleIncrement)
            .add(LaserScan.FIELD_TIME_INCREMENT, timeIncrement)
            .add(LaserScan.FIELD_TIME_SCAN, timeScan)
            .add(LaserScan.FIELD_RANGE_MIN, rangeMin)
            .add(LaserScan.FIELD_RANGE_MAX, rangeMax)
            .add(LaserScan.FIELD_RANGES,
                    Json.createReader(
                        new StringReader(Arrays.toString(ranges))).readArray())
            .add(LaserScan.FIELD_INTENSITIES,
                    Json.createReader(
                            new StringReader(Arrays.toString(intensities))).readArray())
        .build(), LaserScan.TYPE);

        this.header = header;
        this.angleMin = angleMin;
        this.angleMax = angleMax;
        this.angleIncrement = angleIncrement;
        this.timeIncrement = timeIncrement;
        this.timeScan = timeScan;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;

        // copy the array
        this.ranges = new float[ranges.length];
        System.arraycopy(ranges, 0, this.ranges, 0, ranges.length);

        this.intensities = new float[intensities.length];
        System.arraycopy(intensities, 0, this.intensities, 0, intensities.length);
    }

    public Header getHeader(){
        return header;
    }

    public String getTYPE() {
        return TYPE;
    }

    public float getAngleMin() {
        return angleMin;
    }

    public float getAngleMax() {
        return angleMax;
    }

    public float getAngleIncrement() {
        return angleIncrement;
    }

    public float getTimeIncrement() {
        return timeIncrement;
    }

    public float getTimeScan() {
        return timeScan;
    }

    public float getRangeMin() {
        return rangeMin;
    }

    public float getRangeMax() {
        return rangeMax;
    }

    public float[] getRanges() {
        return ranges;
    }

    public float[] getIntensities() {
        return intensities;
    }

    /**
     * Create a clone of this LaserScan.
     */
    @Override
    public LaserScan clone() {
        return new LaserScan(
                this.header,
                this.angleMin,
                this.angleMax,
                this.angleIncrement,
                this.timeIncrement,
                this.timeScan,
                this.rangeMin,
                this.rangeMax,
                this.ranges,
                this.intensities);
    }

}