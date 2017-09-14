package edu.wpi.rail.jrosbridge.messages.sensor_msgs;

import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.messages.std.Header;

import javax.json.Json;

/**
 * Created by jabrena on 12/9/17.
 */
public class Range extends Message {

    public static final String TYPE = "sensor_msgs/Range";
    public static final String DEFINITION =
            "# Single range reading from an active ranger that emits energy and reports\n" +
            "# one range reading that is valid along an arc at the distance measured. \n" +
            "# This message is  not appropriate for laser scanners. See the LaserScan\n" +
            "# message if you are working with a laser scanner.\n\n" +
            "# This message also can represent a fixed-distance (binary) ranger.  This\n" +
            "# sensor will have min_range===max_range===distance of detection.\n" +
            "# These sensors follow REP 117 and will output -Inf if the object is detected\n" +
            "# and +Inf if the object is outside of the detection range.\n\n" +
            "Header header           " +
            "# timestamp in the header is the time the ranger\n                        " +
            "# returned the distance reading\n\n" +
            "# Radiation type enums\n" +
            "# If you want a value added to this list, send an email to the ros-users list\n" +
            "uint8 ULTRASOUND=0\n" +
            "uint8 INFRARED=1\n\n" +
            "uint8 radiation_type    " +
            "# the type of radiation used by the sensor\n                        " +
            "# (sound, IR, etc) [enum]\n\n" +
            "float32 field_of_view   " +
            "# the size of the arc that the distance reading is\n                        " +
            "# valid for [rad]\n                        " +
            "# the object causing the range reading may have\n                        " +
            "# been anywhere within -field_of_view/2 and\n                        " +
            "# field_of_view/2 at the measured range. \n                        " +
            "# 0 angle corresponds to the x-axis of the sensor.\n\nfloat32 min_range       " +
            "# minimum range value [m]\n" +
            "float32 max_range       " +
            "# maximum range value [m]\n                        " +
            "# Fixed distance rangers require min_range==max_range\n\n" +
            "float32 range           " +
            "# range data [m]\n                        " +
            "# (Note: values < range_min or > range_max\n                        " +
            "# should be discarded)\n                        " +
            "# Fixed distance rangers only output -Inf or +Inf.\n                        " +
            "# -Inf represents a detection within fixed distance.\n                        " +
            "# (Detection too close to the sensor to quantify)\n                        " +
            "# +Inf represents no detection within the fixed distance.\n                        " +
            "# (Object out of range)";

    public static final byte ULTRASOUND = 0;
    public static final byte INFRARED = 1;

    private final Header header;
    private final byte radiationType;
    private final float fieldOfView;
    private final float minRange;
    private final float maxRange;
    private final float range;

    public static final String FIELD_HEADER = "header";
    public static final String FIELD_RADIATION_TYPE = "radiation_type";
    public static final String FIELD_FIELD_OF_VIEW = "field_of_view";
    public static final String FIELD_MIN_RANGE = "min_range";
    public static final String FIELD_MAX_RANGE = "max_range";
    public static final String FIELD_RANGE = "range";

    public Range(
            final Header header,
            final byte radiationType,
            final float fieldOfView,
            final float minRange,
            final float maxRange,
            final float range) {

        // build the JSON object
        super(Json
                .createObjectBuilder()
                .add(Range.FIELD_HEADER, header.toJsonObject())
                .add(Range.FIELD_RADIATION_TYPE, radiationType)
                .add(Range.FIELD_FIELD_OF_VIEW, fieldOfView)
                .add(Range.FIELD_MIN_RANGE, minRange)
                .add(Range.FIELD_MAX_RANGE, maxRange)
                .add(Range.FIELD_RANGE, range)
                .build(), Range.TYPE);

        this.header = header;
        this.radiationType = radiationType;
        this.fieldOfView = fieldOfView;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.range = range;
    }

    public  Header getHeader() {
        return this.header;
    }

    public byte getRadiationType(){
        return this.radiationType;
    }

    public float getFieldOfView() {
        return this.fieldOfView;
    }

    public float getMinRange() {
        return this.minRange;
    }

    public float getMaxRange() {
        return this.maxRange;
    }

    public float getRange() {
        return this.range;
    }

    /**
     * Create a clone of this LaserScan.
     */
    @Override
    public Range clone() {
        return new Range(
            this.header,
            this.radiationType,
            this.fieldOfView,
            this.minRange,
            this.maxRange,
            this.range);
    }
}
