package com.calki.krak;

import java.util.ArrayList;

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
	
	public static UltrasonicSensor ultraSensor;
	public static ColorSensor colorSensor;
	public static TouchSensor touchSensor;
	
	public static DifferentialPilot pilot;
	public static OdometryPoseProvider opp;
	public static Pose pose;
	
	public static int BIALY = 6;
	public static int NIEBIESKI = 2;
	public static int CZARNY = 7;
	
	public static int countNiebieskie=0;
	public static int countBiale=0;
	
	public static int posX = 0;
	public static int posY = 0;
	
	public static ArrayList<EliminacjeRuch> listaRuchow;
	public static ArrayList<EliminacjeWieza> listaWiez;
	
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
					pilot.rotateRight();
					
					if(colorSensor.getColorID()==NIEBIESKI){
						pilot.rotate(-pilot.getAngleIncrement());
						pose = opp.getPose();
						listaWiez.add(new EliminacjeWieza(pose.getX(), pose.getY(), NIEBIESKI));
						countNiebieskie++;
						sprawdzListeWiez();
						break;
					}else if(colorSensor.getColorID()==BIALY){
						pilot.rotate(-pilot.getAngleIncrement());
						pose = opp.getPose();
						listaWiez.add(new EliminacjeWieza(pose.getX(), pose.getY(), BIALY));
						countBiale++;
						sprawdzListeWiez();
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
	
	public static void kolorOpusc(){
		Motor.B.rotate(-110);
	}
	
	public static void kolorPodnies(){
		Motor.B.rotate(110);
	}
	public static void jedzPoSpiraliProste() throws InterruptedException{
		pilot.setTravelSpeed(32);
		pilot.setAcceleration(19);
		pilot.setRotateSpeed(60);
		pilot.travel(130);
		kolorPodnies();
		kolorOpusc();
		pilot.rotate(-88);
		pilot.travel(130);
		kolorPodnies();
		kolorOpusc();
		pilot.rotate(-88);
		pilot.travel(130);
		kolorPodnies();
		kolorOpusc();
		pilot.rotate(-88);
		pilot.travel(100);
		kolorPodnies();
		kolorOpusc();
		pilot.rotate(-88);
		pilot.travel(100);
		kolorPodnies();
		kolorOpusc();
		pilot.rotate(-88);
		pilot.travel(70);
		kolorPodnies();
		kolorOpusc();
		pilot.rotate(-88);
		pilot.travel(70);
		kolorPodnies();
		kolorOpusc();
		pilot.rotate(-88);
		pilot.travel(35);
		kolorPodnies();
		kolorOpusc();
		pilot.rotate(-88);
		pilot.travel(30);
		Sound.twoBeeps();
	}
	
	public static double angleDiff(double currAngle, double newAngle){
	   	 double zm1 = Math.abs(newAngle - currAngle);
	   	 double zm2 = Math.abs(newAngle-currAngle+2*Math.PI);
	   	 double zm3 = Math.abs(newAngle-currAngle-2*Math.PI);
	   	 return Math.min(zm1, Math.min(zm2, zm3));
	    }
	    //---- obie funkcje przyjmuja wartoœci dodatnie, zarówno dla x jak i y --//
	    //zwraca wartoœæ o jak¹ ma obróciæ siê robot aby staæ przodem do docelowych wspó³rzênych x,y
	    public static double setDirection(Pose actualPose, double destX, double destY){
	    	double newAngle = (Math.atan2(actualPose.getLocation().getY()-destY,actualPose.getLocation().getX()+destX));
	    	System.out.println(-(Math.toDegrees(angleDiff(Math.toRadians(actualPose.getHeading()), newAngle))));
	    	return -(Math.toDegrees(angleDiff(Math.toRadians(actualPose.getHeading()), newAngle)));
	    }//u¿ycie:
	    // pilot.rotate(setDirection(pose, 128.0d,128.0d); -> podaje mu aktualna pozycjê oraz punkt w którego strone ma sie obórcic
	    
	    public static double setDistanceToPoint(Pose actualPose, double destX, double destY){
	    	destX = -destX;
	    	double d = Math.sqrt((Math.pow((destX - actualPose.getLocation().getX()),2.0d)+(Math.pow((destY - actualPose.getLocation().getY()), 2.0d))));
	    	return d;
	    }
	
	public static void setupRuchy(){
		listaWiez = new ArrayList<EliminacjeWieza>();
		listaRuchow = new ArrayList<EliminacjeRuch>();
		listaRuchow.add(new EliminacjeRuch(128,0));
		listaRuchow.add(new EliminacjeRuch(128,128));
		listaRuchow.add(new EliminacjeRuch(0,128));
		listaRuchow.add(new EliminacjeRuch(0,32));
		listaRuchow.add(new EliminacjeRuch(96,32));
		listaRuchow.add(new EliminacjeRuch(96,96));
		listaRuchow.add(new EliminacjeRuch(32,96));
		listaRuchow.add(new EliminacjeRuch(32,64));
		listaRuchow.add(new EliminacjeRuch(64,64));
	}
	
	public static void sprawdzListeWiez(){
		int i;
		
		if(countNiebieskie==2 || countBiale==2){
			listaRuchow.clear();
			for(i=0;i<listaWiez.size();i++){
				EliminacjeWieza aktualnaWieza = listaWiez.get(i);
				if(aktualnaWieza.getKolor()==BIALY){
					listaRuchow.add(new EliminacjeRuch(aktualnaWieza.getPozycjaX(), aktualnaWieza.getPozycjaY()));
					listaRuchow.add(new EliminacjeRuch(0, 0));
				}else{
					listaRuchow.add(new EliminacjeRuch(aktualnaWieza.getPozycjaX(), aktualnaWieza.getPozycjaY()));
					listaRuchow.add(new EliminacjeRuch(64, 64));
				}
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		pilot = new DifferentialPilot(8.3d,8.18d,19.2d,Motor.C,Motor.A,true);	  
		opp = new OdometryPoseProvider(pilot);
		pose=opp.getPose();
		
		ultraSensor = new UltrasonicSensor(SensorPort.S1);
		colorSensor = new ColorSensor(SensorPort.S4);
		touchSensor = new TouchSensor(SensorPort.S2);
		
		setupRuchy();
		pilot.setTravelSpeed(15);
	    pilot.setRotateSpeed(45);
	    int i=0;
	    int powrotRuch;
	    EliminacjeRuch aktualnyRuch;
	    while(i<=listaRuchow.size()){
	    	aktualnyRuch=listaRuchow.get(i);
	    	LCD.clear();
	    	System.out.println(aktualnyRuch.getPozycjaX());
	    	System.out.println(aktualnyRuch.getPozycjaY());
	    	System.out.println(pose.getX());
	    	System.out.println(pose.getY());
	    	pilot.rotate(setDirection(pose, aktualnyRuch.getPozycjaX(),aktualnyRuch.getPozycjaY()));
	    	pose=opp.getPose();
	    	pilot.travel(setDistanceToPoint(pose, aktualnyRuch.getPozycjaX(),aktualnyRuch.getPozycjaY()));
	    	pose=opp.getPose();
	    	Button.waitForAnyPress();
	    	i=i+1;
	    }
	    Sound.twoBeeps();
	    
	    /*int i,j;
	    for(i=0;i<=4;i++){
	    	for(j=0;j<=4;j++){
	    		mapa[i][j]=0;
	    	}
	    }
	    pose = opp.getPose();
	    */
	    //jedzDoPrzodu(32);
	    
	    //jedzPoSpiraliProste();
		//jedzDoWiezy(true);
		//wiezaZbierzObrot();
		//lcdPokazMape();
		
	    //lcdPokazOdleglosc();
	    //lcdPokazColorId();
	}

}
