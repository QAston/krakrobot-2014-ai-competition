package com.calki.krak;

import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorConstants;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class Eliminacje {
	public static DifferentialPilot pilot;
	public static UltrasonicSensor ultraSensor;
	public static ColorSensor colorSensor;
	
	public static void lcdPokazOdleglosc(){
		   while(true){
		      int value = ultraSensor.getDistance();
		      LCD.clear();
		      LCD.drawInt(value, 0, 3);
		      LCD.refresh();
		   }
	}
	
	public static void lcdPokazColorId(){
		while(true){
			int value = colorSensor.getColorID();
			LCD.clear();
		    LCD.drawInt(value, 0, 3);
		    LCD.refresh();
		}
	}
	
	public static void kolorOpusc(){
		Motor.B.rotate(-110);
	}
	
	public static void kolorPodnies(){
		Motor.B.rotate(110);
	}
	
	public static void jedzDoWiezy() throws InterruptedException{
		while(ultraSensor.getDistance()>5){
			if(ultraSensor.getDistance()<=10){				
				pilot.stop();
				kolorPodnies();
				double pamietajRotate = pilot.getRotateSpeed();
				pilot.setRotateSpeed(20);
				while(colorSensor.getColorID()!=2 || colorSensor.getColorID()!=7){
					pilot.rotateRight();
				}
				pilot.stop();
				pilot.setRotateSpeed(pamietajRotate);
				colorSensor.getColorID();
				kolorOpusc();
				break;
			}
		      pilot.forward();
		   }
	}
	
	public static void main(String[] args) throws InterruptedException {
		pilot = new DifferentialPilot(8.3d,8.18d,19.2d,Motor.C,Motor.A,true);	  
		ultraSensor = new UltrasonicSensor(SensorPort.S1);
		colorSensor = new ColorSensor(SensorPort.S4, SensorConstants.TYPE_LIGHT_ACTIVE);
		pilot.setTravelSpeed(15);
	    pilot.setRotateSpeed(45);
		//jedzDoWiezy();
	    //lcdPokazOdleglosc();
	    lcdPokazColorId();
	}

}
