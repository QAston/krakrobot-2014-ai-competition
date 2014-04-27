package com.calki.krak;

import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DifferentialPilot;

public class RuchaczImpl implements Ruchacz {
	
	DifferentialPilot pilot;
	PoseProvider pp;
	
	RuchaczImpl(DifferentialPilot pilot, PoseProvider pp)
	{
		this.pilot = pilot;
		this.pp = pp;
	}
	
	public void forward()
	{
	     pilot.travel(32);
	}
	
	public void turnLeft()
	{
	     pilot.rotate(-90);
	}
	
	public void turnRight()
	{
	     pilot.rotate(90);
	}

}
