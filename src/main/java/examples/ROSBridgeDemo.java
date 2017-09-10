package examples;

import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Service;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.callback.TopicCallback;
import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.services.ServiceRequest;
import edu.wpi.rail.jrosbridge.services.ServiceResponse;

/**
 * Created by jabrena on 13/7/17.
 */
public class ROSBridgeDemo {

    public static void main(String[] args) throws InterruptedException {
        Ros ros = new Ros("192.168.1.198");
        ros.connect();

        Topic echo = new Topic(ros, "/echo", "std_msgs/String");
        Message toSend = new Message("{\"data\": \"hello, world!\"}");
        echo.publish(toSend);

        Topic echoBack = new Topic(ros, "/echo_back", "std_msgs/String");
        echoBack.subscribe(new TopicCallback() {
            @Override
            public void handleMessage(Message message) {
                System.out.println("From ROS: " + message.toString());
            }
        });


        //Topic echo = new Topic(ros, "/echo", "std_msgs/String");
        //Message toSend = new Message("{\"data\": \"hello, world!\"}");
        //echo.publish(toSend);

        //Service addTwoInts = new Service(ros, "/add_two_ints", "rospy_tutorials/AddTwoInts");

        //ServiceRequest request = new ServiceRequest("{\"a\": 10, \"b\": 20}");
        //ServiceResponse response = addTwoInts.callServiceAndWait(request);
        //System.out.println(response.toString());

        boolean flag = true;
        while (flag == true){
            echo.publish(toSend);
        }

        ros.disconnect();
    }

}