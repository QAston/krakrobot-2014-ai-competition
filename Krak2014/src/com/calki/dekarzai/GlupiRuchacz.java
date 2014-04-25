package com.calki.dekarzai;

import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;

public class GlupiRuchacz implements Ruchacz {
	
	private DifferentialPilot pilot;
	
	private Dzialo dzialo;
	private Mapa mapa;
	private Kierowca kierowca;

	public GlupiRuchacz(Dzialo dzialo, Kierowca kierowca, Mapa mapa) {
		this.dzialo = dzialo;
		this.kierowca = kierowca;
		this.mapa = mapa;
		pilot = new DifferentialPilot(5.4f, 10.5f, Motor.A, Motor.B, false);
		pilot.setRotateSpeed(55);
		pilot.setTravelSpeed(55);
		pilot.setAcceleration(30);
	}

	@Override
	public void jedzDoPrzodu() {		
		pilot.travel(27);
	}

	@Override
	public void jedzDoTylu() {
		pilot.travel(-27);
		pilot.rotate(-180);
	}

	@Override
	public void jedzWPrawo() {
		pilot.rotate(-90);		
	}

	@Override
	public void jedzWLewo() {
		pilot.rotate(90);
	}

}
