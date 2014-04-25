package com.calki.krak;

import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorConstants;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class Eliminacje {
	public static DifferentialPilot pilot;
	public static UltrasonicSensor ultraSensor;
	public static ColorSensor colorSensor;
	
	public static int BIALY = 6;
	public static int NIEBIESKI = 2;
	
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
			if(ultraSensor.getDistance()<=12){				
				pilot.stop();
				kolorPodnies();
				double pamietajRotate = pilot.getRotateSpeed();
				pilot.setRotateSpeed(15);
				while(colorSensor.getColorID()!=NIEBIESKI || colorSensor.getColorID()!=BIALY){
					LCD.drawString(Float.toString(pilot.getAngleIncrement()), 0, 0);
					pilot.rotateRight();
					
					if(colorSensor.getColorID()==NIEBIESKI){
						Sound.beep();
						pilot.rotate(-pilot.getAngleIncrement());
						break;
					}else if(colorSensor.getColorID()==BIALY){
						Sound.twoBeeps();
						pilot.rotate(-pilot.getAngleIncrement());
						break;
					}
					
				}
				pilot.stop();
				pilot.setRotateSpeed(pamietajRotate);
				kolorOpusc();
				break;
			}
		      pilot.forward();
		   }
	}
	
	public static void szukajWiezy(){
		//while()
	}
	
	public static void main(String[] args) throws InterruptedException {
		pilot = new DifferentialPilot(8.3d,8.18d,19.2d,Motor.C,Motor.A,true);	  
		ultraSensor = new UltrasonicSensor(SensorPort.S1);
		colorSensor = new ColorSensor(SensorPort.S4, SensorConstants.TYPE_COLORFULL);
		pilot.setTravelSpeed(15);
	    pilot.setRotateSpeed(45);
		jedzDoWiezy();
	    //lcdPokazOdleglosc();
	    //lcdPokazColorId();
	}

}
