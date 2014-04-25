package com.calki.krak;

import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Pose;

public class Eliminacje {
	public static DifferentialPilot pilot;
	public static UltrasonicSensor ultraSensor;
	public static ColorSensor colorSensor;
	public static TouchSensor touchSensor;
	
	public static OdometryPoseProvider opp;
	public static Pose pose;
	
	public static int BIALY = 6;
	public static int NIEBIESKI = 2;
	public static int CZARNY = 7;
	
	public static int posX = 0;
	public static int posY = 0;
	
	public static int mapa[][] = new int[5][5];
	
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
					LCD.drawInt(mapa[i][j], i, j);
				}
			}
			LCD.drawInt(posX, 1, 5);
			LCD.drawInt(posY, 1, 6);
			Button.waitForAnyPress();
	}
	
	public static int mapGetDoubleTowers(){
		int countNiebieski=0;
		int countBialy=0;
		int i,j;
		for(i=0;i<=4;i++){
			for(j=0;j<=4;j++){
				if(mapa[i][j]==NIEBIESKI){
					countNiebieski++;
				}else if(mapa[i][j]==BIALY){
					countBialy++;
				}
			}
		}
		if(countNiebieski>countBialy){
			return NIEBIESKI;
		}else{
			return BIALY;
		}
	}
	
	public static void kolorOpusc(){
		Motor.B.rotate(-110);
	}
	
	public static void kolorPodnies(){
		Motor.B.rotate(110);
	}
	
	public static void jedzDoWiezy(boolean czyWrocic) throws InterruptedException{
		int dotykStatus=0;
		while(ultraSensor.getDistance()>5){
			if(touchSensor.isPressed()){
					pilot.rotate(-35);
					pilot.travel(7);
				dotykStatus=1;
			}
			if(ultraSensor.getDistance()<=12 || dotykStatus==1){				
				pilot.stop();
				kolorPodnies();
				double pamietajRotate = pilot.getRotateSpeed();
				pilot.setRotateSpeed(15);
				while(colorSensor.getColorID()!=NIEBIESKI || colorSensor.getColorID()!=BIALY){
					LCD.drawString(Float.toString(pilot.getAngleIncrement()), 0, 0);
					pilot.rotateRight();
					
					if(colorSensor.getColorID()==NIEBIESKI){
						Sound.beep();
						//pose = opp.getPose();
						mapaUstawWieze(NIEBIESKI);
						pilot.rotate(-pilot.getAngleIncrement());
						break;
					}else if(colorSensor.getColorID()==BIALY){
						Sound.twoBeeps();
						//pose = opp.getPose();
						mapaUstawWieze(BIALY);
						pilot.rotate(-pilot.getAngleIncrement());
						break;
					}
					
				}
				if(dotykStatus==1){
					pilot.travel(-5);
					pilot.rotate(41);
				}
				pilot.stop();
				pilot.setRotateSpeed(pamietajRotate);
				kolorOpusc();
				if(czyWrocic==true){
					while(colorSensor.getColorID()!=CZARNY){
						pilot.backward();
					}
					pilot.travel(-6);
				}
				break;
			}
		      pilot.forward();
		   }
	}
	
	public static void mapaUstawWieze(int typWiezy){	
		posX=(int)(opp.getPose().getX()) / 29;
		posY=4 - (int)(opp.getPose().getY()) / 29;
		mapa[posX][posY]=typWiezy;
	}
	
	public static void szukajWiezy() throws InterruptedException{
		if(ultraSensor.getDistance()>20 && ultraSensor.getDistance()<75){
			jedzDoWiezy(true);
		}
	}
	
	public static void wiezaZbierzObrot(){
		pilot.travel(6);
		pilot.rotate(360);
	}
	
	public static void jedzDoPrzodu(int wartosc) throws InterruptedException{
		pilot.travel(wartosc);
		if(ultraSensor.getDistance()<=70){
			jedzDoWiezy(true);
		}
	}
	
	public static void jedzPoSpiraliProste() throws InterruptedException{
		pilot.travel(32);
		pilot.rotate(-90);
		pilot.travel(32);
		pilot.travel(32);
		pilot.rotate(-45);
		pilot.travel(45);
		pilot.rotate(-45);
		pilot.travel(32);
		pilot.travel(32);
		pilot.rotate(-45);
		pilot.travel(45);
		pilot.rotate(-45);
		pilot.travel(32);
		pilot.travel(32);
		pilot.rotate(-45);
		pilot.travel(45);
		pilot.rotate(-45);
		pilot.travel(32);
		pilot.rotate(-45);
		pilot.travel(45);
		pilot.rotate(-45);
		pilot.travel(45);
		pilot.rotate(-45);
		Sound.twoBeeps();
	}
	
	public static void main(String[] args) throws InterruptedException {
		pilot = new DifferentialPilot(8.3d,8.18d,19.2d,Motor.C,Motor.A,true);	  
		opp = new OdometryPoseProvider(pilot);
		ultraSensor = new UltrasonicSensor(SensorPort.S1);
		colorSensor = new ColorSensor(SensorPort.S4);
		touchSensor = new TouchSensor(SensorPort.S2);
		pilot.setTravelSpeed(15);
	    pilot.setRotateSpeed(45);
	    int i,j;
	    for(i=0;i<=4;i++){
	    	for(j=0;j<=4;j++){
	    		mapa[i][j]=0;
	    	}
	    }
	    pose = opp.getPose();
	    
	    //jedzDoPrzodu(32);
	    
	    jedzPoSpiraliProste();
		//jedzDoWiezy(true);
		//wiezaZbierzObrot();
		//lcdPokazMape();
		
	    lcdPokazOdleglosc();
	    //lcdPokazColorId();
	}

}
