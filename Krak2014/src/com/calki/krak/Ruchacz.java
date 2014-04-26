package com.calki.krak;

import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DifferentialPilot;

public class Ruchacz {
	
	DifferentialPilot pilot;
	PoseProvider pp;
	
	Ruchacz(DifferentialPilot pilot, PoseProvider pp)
	{
		this.pilot = pilot;
		this.pp = pp;
	}
	
	void forward()
	{
	     pilot.travel(32);
	}
	
	void turnLeft()
	{
	     pilot.rotate(-90);
	}
	
	void turnRight()
	{
	     pilot.rotate(90);
	}

}
