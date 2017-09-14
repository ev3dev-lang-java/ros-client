package edu.wpi.rail.jrosbridge.messages.sensor_msgs;

import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.messages.std.Header;
import edu.wpi.rail.jrosbridge.primitives.Time;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonObject;


/**
 * Created by jabrena on 14/9/17.
 */
public class RangeTest {

    @Test
    public void rangeTest(){

        final Header header = new Header(0, Time.now(), "demo");
        final Message message = new Range(
                header,
                Range.INFRARED,
                0.05f,
                0.05f,
                0.8f,
                20f
        );


        JsonObject demo = Json
                .createObjectBuilder()
                .add(Range.FIELD_HEADER, header.toJsonObject())
                .add(Range.FIELD_RADIATION_TYPE, Range.INFRARED)
                .add(Range.FIELD_FIELD_OF_VIEW, 0.05f)
                .add(Range.FIELD_MIN_RANGE, 0.05f)
                .add(Range.FIELD_MAX_RANGE, 0.8f)
                .add(Range.FIELD_RANGE, 0.4f)
                .build();

        System.out.println(demo.toString());
    }

}