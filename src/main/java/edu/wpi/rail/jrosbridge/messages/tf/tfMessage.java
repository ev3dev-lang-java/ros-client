package edu.wpi.rail.jrosbridge.messages.tf;

import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.messages.geometry.TransformStamped;

import javax.json.Json;
import java.io.StringReader;
import java.util.Arrays;

public class tfMessage extends Message {

    public static final java.lang.String TYPE = "tf/tfMessage";
    public static final java.lang.String DEFINITION = "geometry_msgs/TransformStamped[] transforms\n";

    public static final String FIELD_TRANSFORMS = "transforms";

    private final TransformStamped[] transformStamped;

    public tfMessage(final TransformStamped[] transformStamped) {

        // build the JSON object
        super(Json
                .createObjectBuilder()
                .add(tfMessage.FIELD_TRANSFORMS,
                        Json.createReader(
                                new StringReader(Arrays.toString(transformStamped))).readArray())
                .build(), tfMessage.TYPE);

        this.transformStamped = transformStamped;
    }

    public TransformStamped[] getTransforms(){
        return transformStamped;
    }
}