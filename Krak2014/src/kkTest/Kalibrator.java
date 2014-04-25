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
	
	//oblicza ró¿nicê miêdzy k¹tami w radianach
    public static double angleDiff(double currAngle, double newAngle){
   	 double zm1 = Math.abs(newAngle - currAngle);
   	 double zm2 = Math.abs(newAngle-currAngle+2*Math.PI);
   	 double zm3 = Math.abs(newAngle-currAngle-2*Math.PI);
   	 return Math.min(zm1, Math.min(zm2, zm3));
    }
    //zwraca wartoœæ o jak¹ ma obróciæ siê robot aby staæ przodem do docelowych wspó³rzênych x,y
    public static double goToPoint(Pose actualPose, double destX, double destY){
    	double newAngle = (Math.atan2(actualPose.getLocation().getY()-destY,actualPose.getLocation().getX()-destX));
    	return Math.toDegrees(angleDiff(Math.toRadians(actualPose.getHeading()), newAngle));
    }
    
	 public static void main(String[] args){
		 /*DifferentialPilot pilot = new DifferentialPilot(8.3f,2.0f,2.5f,Motor.C,Motor.A,true);
		 pilot.setTravelSpeed(45.0f);
		 pilot.forward();
		 Button.waitForAnyPress();*/
		 
		 DifferentialPilot pilot = new DifferentialPilot(8.3d,8.14d,18.6d,Motor.C,Motor.A,true);//dla voltage 8.17
	     OdometryPoseProvider opp = new OdometryPoseProvider(pilot);
	     ColorSensor kolor = new ColorSensor(SensorPort.S4);
	    
	     
	     pilot.setTravelSpeed(15);
	     pilot.setRotateSpeed(35);
	     
	     
	     Pose pose = opp.getPose();
	     System.out.print(Battery.getVoltage());

	     /*pilot.travel(32);
	     pose = opp.getPose();
	     pilot.travel(32);
	     pose = opp.getPose();*/
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
	     LCD.drawString(Double.toString(pose.getLocation().getX()), 0, 1);
	     LCD.drawString(Double.toString(pose.getLocation().getY()), 0, 2);
	     LCD.drawString(Float.toString(pose.getHeading()), 0, 3);
	     //double newAngle = ((Math.atan2(pose.getLocation().getY(),pose.getLocation().getX()))*180)/Math.PI;//stopnie
	     //double newAngle = (Math.atan2(pose.getLocation().getY(),pose.getLocation().getX()));//radiany
	     //LCD.drawString(Double.toString(newAngle), 0, 4);
	     //LCD.drawString(Double.toString(angleDiff(Math.toRadians(pose.getHeading()), newAngle)), 0, 5);
	     /*pilot.rotateLeft();
	     while(Math.abs(pose.getHeading()-(float)pi) >= 10){
	    	 LCD.clear();
	    	 pose = opp.getPose();
	     }
	     pilot.quickStop();*/
	     //pilot.rotate(Math.toDegrees(angleDiff(Math.toRadians(pose.getHeading()), newAngle)));
	     pilot.rotate(goToPoint(pose, 0d, 0d));

	     
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
