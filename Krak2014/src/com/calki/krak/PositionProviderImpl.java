package com.calki.krak;

import lejos.robotics.localization.PoseProvider;

public class PositionProviderImpl implements PositionProvider {
	PoseProvider pp;
	PositionProviderImpl(PoseProvider pp)
	{
		this.pp = pp;
	}
	public Position getMapPosition()
	{
		return Position.get(4 - (int)(pp.getPose().getX()) / 32, 4 + (int)(pp.getPose().getY()) / 32);
	}
}
