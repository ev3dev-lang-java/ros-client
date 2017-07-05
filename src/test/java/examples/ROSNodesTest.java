package examples;

import ev3dev.ros.nodes.BatteryNode;
import ev3dev.ros.nodes.EV3IRSensorNode;
import ev3dev.ros.nodes.EV3UltrasonicSensorNode;
import ev3dev.ros.nodes.LaserScanNode;
import ev3dev.sensors.Button;
import lejos.hardware.port.SensorPort;
import org.ros.RosCore;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class ROSNodesTest {

    private static RosCore mRosCore;

    public static void main(final String [] args) throws UnknownHostException {

        //Connect with Roscore
        connectWithRoscore(args);

        final NodeMainExecutor nodeExecutor = DefaultNodeMainExecutor.newDefault();

        System.out.println("Starting ROS nodes");
        final String nodeName = "BrickNode";
        final NodeConfiguration nodeConfig = getNodeConfig(nodeName);


        final NodeMain batteryPublisherNode = new BatteryNode();
        final NodeMain ev3irPublisherNode = new EV3IRSensorNode(SensorPort.S1);
        final NodeMain ev3usPublisherNode = new EV3UltrasonicSensorNode(SensorPort.S2);
        final NodeMain laserPublisherNode = new LaserScanNode("/dev/ttyUSB0");

        nodeExecutor.execute(batteryPublisherNode, nodeConfig);
        nodeExecutor.execute(ev3irPublisherNode, nodeConfig);
        nodeExecutor.execute(ev3usPublisherNode, nodeConfig);
        nodeExecutor.execute(laserPublisherNode, nodeConfig);

        Button.waitForAnyEvent();
    }

    private static String readRoscoreAddress(final String[] args) {

        System.out.println("Reading IP from roscore");
        String address = "";
        if (args.length > 0) {
            address = args[0];
            System.out.println("roscore IP: " + address);
        }else {
            throw new IllegalArgumentException("roscore IP not provided");
        }

        return address;
    }

    private static void connectWithRoscore(final String[] args) {
        final String address = readRoscoreAddress(args);
        mRosCore = RosCore.newPublic(address, 11311);
        mRosCore.start();
        try {
            mRosCore.awaitStart(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Ros core started");
    }

    private static NodeConfiguration getNodeConfig(final String nodeName) throws UnknownHostException {

        System.out.println("Nodename: " + nodeName);

        final String address = Inet4Address.getLocalHost().getHostAddress();
        System.out.println("Brick address: " + address);
        //NodeConfiguration talkerConfig = NodeConfiguration.newPublic(Inet4Address.getLocalHost().getHostAddress());

        //Get HostName
        final String hostName = "ev3dev";
        System.out.println("Brick hostname: " + hostName);
        NodeConfiguration nodeConfig = NodeConfiguration.newPublic(hostName);
        nodeConfig.setMasterUri(mRosCore.getUri());
        nodeConfig.setNodeName(nodeName);

        return nodeConfig;
    }

}
