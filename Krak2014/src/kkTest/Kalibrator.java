package kkTest;

import lejos.nxt.Battery;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.*;

public class Kalibrator {
	 public static void main(String[] args){
		 /*DifferentialPilot pilot = new DifferentialPilot(8.3f,2.0f,2.5f,Motor.C,Motor.A,true);
		 pilot.setTravelSpeed(45.0f);
		 pilot.forward();
		 Button.waitForAnyPress();*/
		 
		 DifferentialPilot pilot = new DifferentialPilot(8.3d,8.1d,18.3d,Motor.C,Motor.A,true);//dla voltage 8.57
	     OdometryPoseProvider opp = new OdometryPoseProvider(pilot);
	     ColorSensor kolor = new ColorSensor(SensorPort.S4);
	    
	     
	     pilot.setTravelSpeed(15);
	     pilot.setRotateSpeed(35);
	     
	     
	     Pose pose = opp.getPose();
	     System.out.print(Battery.getVoltage());
	     LCD.drawString(Double.toString(pose.getLocation().getX()), 0, 1);
	     LCD.drawString(Double.toString(pose.getLocation().getY()), 0, 2);
	     LCD.drawString(Float.toString(pose.getHeading()), 0, 3);
	     pilot.travel(32);
	     pose = opp.getPose();
	     pilot.travel(32);
	     pose = opp.getPose();
	     pilot.travel(32);
	     pose = opp.getPose();
	     pilot.rotate(90);
	     pose = opp.getPose();
	     pilot.travel(32);
	     pose = opp.getPose();
	     pilot.rotate(90);
	     pose = opp.getPose();
	     pilot.travel(32);
	     pose = opp.getPose();
	     LCD.drawString(Double.toString(pose.getLocation().getX()), 0, 4);
	     LCD.drawString(Double.toString(pose.getLocation().getY()), 0, 5);
	     LCD.drawString(Float.toString(pose.getHeading()), 0, 6);
	     
	     //pilot.rotate((double)diff);
	     /*LCD.drawString(Double.toString(pose.getLocation().getX()), 0, 0);
	     LCD.drawString(Double.toString(pose.getLocation().getY()), 0, 1);
	     LCD.drawString(Float.toString(pose.getHeading()), 0, 2);
	     
	     pilot.travel(32);
	     
	     pose = opp.getPose();
	     pilot.rotate(-90);
	     pose = opp.getPose();
	     pilot.travel(32);
	     //pilot.stop();
	     pose = opp.getPose();
	     pilot.rotate(-90);
	     pose = opp.getPose();
	     pilot.travel(32);
	     //pilot.stop();
	     pose = opp.getPose();
	     pilot.rotate(-90);
	     pose = opp.getPose();
	     pilot.travel(32);
	     pose = opp.getPose();
	     pilot.rotate(-90);
	     pose = opp.getPose();
	    // pilot.travel(32);
	     
	     LCD.drawString(Double.toString(pose.getLocation().getX()), 0, 3);
	     LCD.drawString(Double.toString(pose.getLocation().getY()), 0, 4);
	     LCD.drawString(Float.toString(pose.getHeading()), 0, 5);*/
	     
	     
	     Button.waitForAnyPress();
	 }

}
