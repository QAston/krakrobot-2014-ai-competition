package com.calki.krak;

import lejos.nxt.Motor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;

public class Final {

	final DifferentialPilot pilot;
	final OdometryPoseProvider opp;
	final PositionProvider position;
	final Mapa mapa;
	final Ruchacz ruchacz;
	
	Final()
	{
		pilot = new DifferentialPilot(8.3d,8.2d,18.5d,Motor.C,Motor.A,true);
	    pilot.setTravelSpeed(15);
	    pilot.setRotateSpeed(35);
		opp = new OdometryPoseProvider(pilot);
		position = new PositionProvider(opp);
		mapa = new Mapa();
		
		ruchacz = new Ruchacz(pilot, opp);
		
		
	}
	
	void run()
	{
		while(true)
		{
			ruchacz.forward();
		}
	}
	
	
	public static void main(String[] args) {

	    Final f = new Final();
	    f.run();
	}
}
