package com.calki.krak;

import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorConstants;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Pose;

public class Eliminacje {
	public static DifferentialPilot pilot;
	public static UltrasonicSensor ultraSensor;
	public static ColorSensor colorSensor;
	
	public static OdometryPoseProvider opp;
	public static Pose pose;
	
	public static int BIALY = 6;
	public static int NIEBIESKI = 2;
	public static int CZARNY = 7;
	
	public static int posX = 4;
	public static int posY = 4;
	
	public static int mapa[][];
	
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
	
	public static void lcdPokazMape(){
			LCD.clear();
			int i,j;
			for(i=0;i<=4;i++){
				for(j=0;j<=4;j++){
					LCD.drawInt(mapa[i][i], i, j);
				}
			}
			LCD.drawInt(posX, 5, 1);
			LCD.drawInt(posY, 6, 1);
			Button.waitForAnyPress();
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
						mapaUstawWieze(NIEBIESKI);
						pilot.rotate(-pilot.getAngleIncrement());
						break;
					}else if(colorSensor.getColorID()==BIALY){
						Sound.twoBeeps();
						mapaUstawWieze(BIALY);
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
	
	public static void mapaUstawWieze(int typWiezy){
		float obecnaPozycjaX=pose.getX();
		float obecnaPozycjaY=pose.getY();
		
		mapa[(int)obecnaPozycjaX%32][(int)obecnaPozycjaY%32]=typWiezy;
	}
	
	public static void szukajWiezy() throws InterruptedException{
		if(ultraSensor.getDistance()>20 && ultraSensor.getDistance()<75){
			jedzDoWiezy();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		pilot = new DifferentialPilot(8.3d,8.18d,19.2d,Motor.C,Motor.A,true);	  
		ultraSensor = new UltrasonicSensor(SensorPort.S1);
		colorSensor = new ColorSensor(SensorPort.S4);
		pilot.setTravelSpeed(15);
	    pilot.setRotateSpeed(45);
	    mapa = new int[][]{
	    		  { 0, 0, 0, 0, 0 },
	    		  { 0, 0, 0, 0, 0 },
	    		  { 0, 0, 0, 0, 0 },
	    		  { 0, 0, 0, 0, 0 },
	    		  { 0, 0, 0, 0, 0 }
	    		};
	    pose = opp.getPose();
	    
	    
		jedzDoWiezy();
		lcdPokazMape();
	    //lcdPokazOdleglosc();
	    //lcdPokazColorId();
	}

}
