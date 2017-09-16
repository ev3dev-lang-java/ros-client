# ROS Support for EV3Dev-Java


## Introduction

This library provide the support to integrate Sensors & Actuators on ROS.

ROS is Distributed system which provides:

* Topics
* Services
* Actions

The library only cover the first element, Topics. 

## Topics

Using this library, the user could publish & subscribe information to ROS for the following topics:

### Topics Published

* battery (std_msgs.Float32)
* scan (sensor_msgs.LaserScan)
 
### Topics Subscribed 
 
* cmd_vel (geometry_msgs/Twist) 
 
## Applications

### Teleoperation

Using information provided by the topic `cmd_vel` it could be possible to teleoperate the robot
using ROS technologies.

### SLAM

Publishing information about the robot and using other ROS Components 
like `Google Cartographer`, the robot could use map information provided by this component
and improve the local navigation capabilities
 
## Current status

At the moment, the library publish messages for the sensor RPLIDAR A2 on a ROS Instance.

To Start a ROS Session:

```
source /opt/ros/kinetic/setup.bash
cd catkin_ws
source install_isolated/setup.bash
roslaunch rosbridge_server rosbridge_websocket.launch
```

 

