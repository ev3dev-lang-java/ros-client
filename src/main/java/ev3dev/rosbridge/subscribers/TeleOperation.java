package ev3dev.rosbridge.subscribers;

import edu.wpi.rail.jrosbridge.Ros;

import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.callback.TopicCallback;
import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.messages.geometry.Twist;
import lejos.robotics.navigation.DifferentialPilot;
import lombok.extern.slf4j.Slf4j;

public @Slf4j class TeleOperation {

    private final Ros ros;
    private final DifferentialPilot pilot;

    public TeleOperation(
            final Ros ros,
            final DifferentialPilot pilot){
        this.ros = ros;
        this.pilot = pilot;
    }

    public void subscribe(){

        Topic twistMessage = new Topic(ros, "/cmd_vel", "geometry_msgs/Twist");
        twistMessage.subscribe(new TopicCallback() {

            @Override
            public void handleMessage(Message message) {
                Twist data = Twist.fromMessage(message);
                double x = data.getLinear().getX();
                double z = data.getAngular().getZ();
                
                if((x == 0d) && (z== 0d)){
                    LOGGER.info("Stop");
                    pilot.stop();
                }else if(x > 0d){
                    LOGGER.info("Forward");
                    if(pilot.isMoving()){
                        pilot.stop();
                    }
                    //pilot.forward();
                    pilot.travel(200d, true);
                }else if(x < 0d) {
                    LOGGER.info("Backward");
                    //pilot.backward();
                    if(pilot.isMoving()){
                        pilot.stop();
                    }
                    pilot.travel(-200d, true);
                }else if(z < 0d) {
                    LOGGER.info("Rotate Right");
                    //pilot.rotateRight();
                    if(pilot.isMoving()){
                        pilot.stop();
                    }
                    pilot.rotate(720d, true);
                }else if(z > 0d) {
                    LOGGER.info("Rotate Left");
                    //pilot.rotateLeft();
                    if(pilot.isMoving()){
                        pilot.stop();
                    }
                    pilot.rotate(-720d, true);
                }

            }

        });
    }

}
