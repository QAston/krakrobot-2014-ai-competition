package com.calki.krak;

import lejos.robotics.localization.PoseProvider;

public class PositionProvider {
	PoseProvider pp;
	PositionProvider(PoseProvider pp)
	{
		this.pp = pp;
	}
	public Position getMapPosition()
	{
		return new Position(4 - (int)(pp.getPose().getX()) / 32, 4 + (int)(pp.getPose().getY()) / 32);
	}
}
