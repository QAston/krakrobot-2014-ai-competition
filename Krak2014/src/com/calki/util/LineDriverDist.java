package com.calki.util;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;

public class LineDriverDist extends LineDriverBase implements MoveListener {

	protected float distToTravel;
	protected boolean used = false;
	public LineDriverDist(DifferentialPilot pilot, PidInfo pidInfo, SensorReader sensor, float distToTravel, float side) {
		super(pilot, pidInfo, sensor);
		this.distToTravel = distToTravel;
		this.side = side;
		this.pilot.addMoveListener(this);
	}
	
	protected float getDistTraveled(Move move)
	{
		return forward ? move.getDistanceTraveled() : -move.getDistanceTraveled();
	}
	@Override
	public void moveStopped(Move event, MoveProvider mp) {
		distToTravel -= getDistTraveled(event);
	}

	@Override
	protected boolean onSteer() {
		return false;
	}
	
	@Override
	public void drive()
	{
		used = true;
		super.drive();
		used = false;
	}
	
	public void reset(float dist)
	{
		forward = dist >= 0;
		if (!forward)
			dist = -dist;
		distToTravel = dist;
		reset();
	}

	@Override
	protected boolean beforeSteer() {
		float t = getDistTraveled(this.pilot.getMovement());
		if (distToTravel - t  <= 0)
			return true;
		return false;
	}

	@Override
	public void moveStarted(Move event, MoveProvider mp) {
	}
}
