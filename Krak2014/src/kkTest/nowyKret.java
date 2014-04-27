package kkTest;

import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.robotics.navigation.DifferentialPilot;

public class nowyKret {

	public static void main(String[] args) {
	
		// TODO Auto-generated method stub
		DifferentialPilot pilot = new DifferentialPilot(4.32d, 12.4, Motor.A, Motor.C,true);
		pilot.setAcceleration(15);
		pilot.setTravelSpeed(25);
		pilot.setRotateSpeed(35);
		pilot.travel(32);
		pilot.rotate(-90);//prawo
		pilot.travel(32);
		pilot.travel(32);
		pilot.rotate(90);
		
		

	}

}
