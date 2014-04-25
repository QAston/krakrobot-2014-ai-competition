package com.calki.krak;

import lejos.nxt.Motor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;

public class Final {

	final DifferentialPilot pilot;
	final OdometryPoseProvider opp;
	final PositionProvider position;
	
	Final()
	{
		pilot = new DifferentialPilot(8.3d,8.2d,18.5d,Motor.C,Motor.A,true);
		opp = new OdometryPoseProvider(pilot);
		position = new PositionProvider(opp);
		
		
	}
	
	
	public static void main(String[] args) {

	    Final f = new Final();
	
	}
}
