package examples;

import ev3dev.ros.nodes.LaserScanNode;
import org.ros.RosCore;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class LaserScanTest {

    private static RosCore mRosCore;

    public static void main(String [] args) throws UnknownHostException {

        System.setProperty("javax.net.debug","all");

        for (String s: args) {
            System.out.println(s);
        }

        //String IP = "localhost";
        String IP = "192.168.1.244";
        if (args.length > 0) {
            IP = args[0];
        }

        mRosCore = RosCore.newPublic(IP, 11311);
        mRosCore.start();
        try {
            mRosCore.awaitStart(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Ros core started");


        NodeMainExecutor e = DefaultNodeMainExecutor.newDefault();

        System.out.println("Starting talker node...");
        //NodeConfiguration talkerConfig = NodeConfiguration.newPublic(Inet4Address.getLocalHost().getHostAddress());
        NodeConfiguration talkerConfig = NodeConfiguration.newPublic("ev3dev");
        talkerConfig.setMasterUri(mRosCore.getUri());
        talkerConfig.setNodeName("Talker");
        NodeMain talker = new LaserScanNode();
        e.execute(talker, talkerConfig);

        //TODO Improve this way
        while (true) {}
    }

}
