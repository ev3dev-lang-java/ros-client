package examples;

import edu.wpi.rail.jrosbridge.Ros;
import ev3dev.actuators.Sound;
import ev3dev.rosbridge.subscribers.TeleOperation;
import lejos.robotics.navigation.DifferentialPilot;

/**
 * Created by jabrena on 13/7/17.
 */
public class PilotDemo {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Running a TeleOperation Demo");

        PilotConfig pilotConfig = new PilotConfig();
        DifferentialPilot pilot = pilotConfig.getPilot();
        pilot.travel(10d,true);
        while(pilot.isMoving()){
            System.out.println("is moving");
        }
        Sound.getInstance().beep();
        pilot.travel(-10d);

        pilot.rotate(90, true);
        while(pilot.isMoving()){
            System.out.println("is moving");
        }
        pilot.rotate(-90);



    }

}