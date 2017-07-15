package edu.wpi.rail.jrosbridge.messages.sensor_msgs;

import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.messages.std.Header;

import javax.json.Json;
import java.io.StringReader;
import java.util.Arrays;

public class LaserScan extends Message {

    public static final String TYPE = "sensor_msgs/LaserScan";

    /**
     * The name of the header field for the message.
     */
    public static final String FIELD_HEADER = "header";

    public static final String FIELD_ANGLE_MIN = "angleMin";
    public static final String FIELD_ANGLE_MAX = "angleMax";
    public static final String FIELD_ANGLE_INCREMENT = "angleIncrement";
    public static final String FIELD_TIME_INCREMENT = "timeIncrement";
    public static final String FIELD_TIME_SCAN = "timeScan";
    public static final String FIELD_RANGE_MIN = "rangeMin";
    public static final String FIELD_RANGE_MAX = "rangeMax";
    public static final String FIELD_RANGES = "ranges";
    public static final String FIELD_INTENSITIES = "intensities";

    final Header header;
    final float angleMin;
    final float angleMax;
    final float angleIncrement;
    final float timeIncrement;
    final float timeScan;
    final float rangeMin;
    final float rangeMax;
    final float[] ranges;
    final float[] intensities;

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