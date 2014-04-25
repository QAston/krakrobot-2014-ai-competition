package kkTest;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.*;

public class Kalibrator {
	 public static void main(String[] args){
		 /*DifferentialPilot pilot = new DifferentialPilot(8.3f,2.0f,2.5f,Motor.C,Motor.A,true);
		 pilot.setTravelSpeed(45.0f);
		 pilot.forward();
		 Button.waitForAnyPress();*/
		 
		 DifferentialPilot pilot = new DifferentialPilot(8.3d,8.2d,18.5d,Motor.C,Motor.A,true);
	     OdometryPoseProvider opp = new OdometryPoseProvider(pilot);
	     
	     
	     pilot.setTravelSpeed(15);
	     pilot.setRotateSpeed(45);
	     
	     Pose pose = opp.getPose();
	     
	     LCD.drawString(Double.toString(pose.getLocation().getX()), 0, 0);
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
	     LCD.drawString(Float.toString(pose.getHeading()), 0, 5);
	     Button.waitForAnyPress();
	 }

}
