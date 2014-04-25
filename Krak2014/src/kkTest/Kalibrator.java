package kkTest;

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
		 
		 DifferentialPilot pilot = new DifferentialPilot(8.3d,8.18d,19.2d,Motor.C,Motor.A,true);
	     OdometryPoseProvider opp = new OdometryPoseProvider(pilot);
	     ColorSensor kolor = new ColorSensor(SensorPort.S2);
	    
	     
	     pilot.setTravelSpeed(15);
	     pilot.setRotateSpeed(35);
	     
	     
	     Pose OldPose = opp.getPose();
	     Pose NewPose;
	     
	     while(kolor.getColorID() != 7){
	    	 pilot.rotateRight();
	     }
	     NewPose = opp.getPose();
	     
	     float diff = OldPose.getHeading() - NewPose.getHeading();
	     
	     pilot.rotate((double)diff);
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
