package com.calki.krak;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class Eliminacje {

	
	
	public static void jedzDoPrzodu(){
		Motor.A.forward();
		Motor.C.forward();
	}
	
	public static void stop(){
		Motor.A.stop();
		Motor.C.stop();
	}
	   
	public static void jedzDoWiezy() throws InterruptedException{
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
		while(us.getDistance()>5){
			if(us.getDistance()<7){				
				stop();
				break;
			}
		      jedzDoPrzodu();
		   }
	}
	
	public static void main(String[] args) throws InterruptedException {
		jedzDoWiezy();
	}

}
