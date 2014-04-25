package com.calki.dekarzai;

import com.calki.util.LineDriverBase;
import com.calki.util.LineDriverDist;
import com.calki.util.PidInfo;

import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;

public class BetterRuchacz implements Ruchacz {
	
	private DifferentialPilot pilot;
	
	private Dzialo dzialo;
	private Mapa mapa;
	private Kierowca kierowca;
	private LineDriverDist lineDriver;
	public final float distPola = (float) 14.5;
	private boolean started = false;

	public BetterRuchacz(Dzialo dzialo, Kierowca kierowca, Mapa mapa) {
		this.dzialo = dzialo;
		this.kierowca = kierowca;
		this.mapa = mapa;
		
		pilot = new DifferentialPilot(5.4f, 10.5f, Motor.A, Motor.B, false);
		final int SPEED = 55;
		final int ACCELERATION = 28;
		pilot.setRotateSpeed(SPEED);
		pilot.setTravelSpeed(SPEED/2);
		pilot.setAcceleration(ACCELERATION);
		
		PidInfo pidInfo = new PidInfo(380, 1, 0, 0.030f, 0, 0.0f, 20, -20.0f, 15f, -15f);
		lineDriver = new LineDriverDist(pilot, pidInfo, this.dzialo.colorSensorReader, 27, -1);
	}

	@Override
	public void jedzDoPrzodu() {
		// w przypadku jazdy prosto zawsze zakladamy, ze robot jest przy jakiejs krawedzi i jest dokladnie w jej polowie
		float distPrzyKrawedzi = distPola;
		if (!started && Plansza.jestNaRoguKrawedzi(kierowca.getAktualnePolozenie()))
		{
			distPrzyKrawedzi -= 9;
			prostoDoKrawedzi(12);
		}
		started = true;
		if (Plansza.jestNaRoguKrawedzi(kierowca.getDocelowePolozenie()))
		{
			prostoPrzyKrawedzi(7);
			prostoZKrawedzi(7, 19);
			return;
		}
		// jazda po samej krawedzi
		else if (Plansza.jestNaKrawedzi(kierowca.getAktualnePolozenie()))
		{
			prostoPrzyKrawedzi(distPrzyKrawedzi);
			pilot.stop();
		}
		// wyjazd z dachu na krawedz
		else if (Plansza.jestNaKrawedzi(kierowca.getDocelowePolozenie()))
		{
			prostoPrzyKrawedzi(7);
			prostoZKrawedzi(7, 17);
		}
		else
		{
			prostoPrzyKrawedzi(distPola);
			KierunekSensora poprzedniKierunekSensora = dzialo.getKierunekSensora();
			dzialo.ustaw(KierunekSensora.SENSOR_PROSTO);
			prostoDoKrawedzi(5.0f);
			dzialo.ustaw(poprzedniKierunekSensora);
			prostoPrzyKrawedzi(-1.0f);
			skretDoBokuLinii(20, dzialo.getKierunekSensora());
		}
		mapa.update(kierowca);
		DebugUtils.wypiszMape(mapa);
	}

	@Override
	public void jedzDoTylu() {
		pilot.rotate(-180);
		prostoPrzyKrawedzi(distPola);
	}

	@Override
	public void jedzWPrawo() {
		skretJazda(KierunekSensora.SENSOR_PRAWO);
		mapa.update(kierowca);
	}

	@Override
	public void jedzWLewo() {
		skretJazda(KierunekSensora.SENSOR_LEWO);
		mapa.update(kierowca);
	}
	
	private void skretJazda(KierunekSensora kierunekSkretu)
	{
		// skret na rogu krawedzi
		if (Plansza.jestNaKrawedzi(kierowca.getAktualnePolozenie())
				&& Plansza.jestNaKrawedzi(kierowca.getDocelowePolozenie())
				&& Plansza.jestNaRoguKrawedzi(kierowca.getAktualnePolozenie())) {
			float dist = distPola;
			if (kierunekSkretu == dzialo.getKierunekSensora())
				dist -= 8;
			dzialo.ustaw(kierunekSkretu);
			skretDoLinii(90, kierunekSkretu);
			prostoPrzyKrawedzi(dist);
		}
		else
		{
			// skret moze byc wykonany gdy robot jest przy jakiejs linii, badz nie
			// nie jest przy krawedzi w 2 przypadkach: zakret z rogu i zakret po wyjezdzie na krawedz
			boolean przyLinii = !Plansza.jestNaKrawedzi(kierowca.getAktualnePolozenie());
			// skret moze byc wykonany na 2 sposoby: robot jest blisko (przy krawedzi tego pola) lub daleko do sasiedniego pola (przy krawedzi prostopadlej do pola)
			boolean przyKrawedziDocelowegoPola = kierunekSkretu == dzialo.getKierunekSensora();
			
			// skret moze byc wykonany na 2 sposoby zaleznie od tego, czy robot ma dostepna linie na koncu docelowego pola czy nie
			boolean poleDoceloweZakonczoneLinia = !Plansza.jestNaKrawedzi(kierowca.getDocelowePolozenie());
			
			dzialo.ustaw(kierunekSkretu);
			if (poleDoceloweZakonczoneLinia)
			{
				if (przyKrawedziDocelowegoPola)
				{
					dokret(60, kierunekSkretu);
					dokretDoBokuLinii(40, kierunekSkretu);

					prostoPrzyKrawedzi(5.0f);
					if (dzialo.czyWidziCzarny())
						skretZLinii(15, kierunekSkretu.przeciwny());
					dzialo.ustaw(KierunekSensora.SENSOR_PROSTO);
					prostoDoKrawedzi(5.0f);
					dzialo.ustaw(kierunekSkretu);
					prostoPrzyKrawedzi(-1.0f);
					skretDoBokuLinii(30, kierunekSkretu);
				}
				else
				{
					prostoPrzyKrawedzi(2.0f);
					dzialo.ustaw(kierunekSkretu);
					skret(90, kierunekSkretu);
					dokretDoBokuLinii(10, kierunekSkretu);
					prostoPrzyKrawedzi(10.0f);
					dzialo.ustaw(KierunekSensora.SENSOR_PROSTO);
					prostoDoKrawedzi(5.0f);
					dzialo.ustaw(kierunekSkretu);
					prostoPrzyKrawedzi(-1.0f);
					skretDoBokuLinii(10, kierunekSkretu);
				}
			}
			else
			{
				// wyjechanie z planszy poprzez skret
				if (przyLinii)
				{
					skret(90, kierunekSkretu);
					pilot.travel(16, false);
					dzialo.ustaw(kierunekSkretu.przeciwny());
					skretDoBokuLinii(220, kierunekSkretu.przeciwny());
					dzialo.ustaw(kierunekSkretu);
					skret(90, kierunekSkretu);
				}
				// skret na krawedzi po wyjechaniu z planszy
				else
				{
					//TODO!
					if (!Plansza.jestNaRoguKrawedzi(kierowca.getDocelowePolozenie()))
					{
						skretDoBokuLinii(100, kierunekSkretu);
						prostoPrzyKrawedzi(8);
					}
					else
					{
						skret(90, kierunekSkretu);
						prostoPrzyKrawedzi(distPola);
					}
				}
			}
		}
	}
	
	private void dokret(float stopnie, KierunekSensora kierunekSkretu)
	{
		int mnoznik = (kierunekSkretu == KierunekSensora.SENSOR_PRAWO) ? -1
				: 1;
		pilot.steer(100 * mnoznik, stopnie * mnoznik);
	}
	
	private void dokretDoLinii(float stopnie, KierunekSensora kierunekSkretu)
	{
		int mnoznik = (kierunekSkretu == KierunekSensora.SENSOR_PRAWO) ? -1
				: 1;
		double old = pilot.getTravelSpeed();
		pilot.setTravelSpeed(6);
		pilot.steer(100 * mnoznik, stopnie* mnoznik, true);
		while (!dzialo.czyWidziCzarny() && pilot.isMoving())
			;
		pilot.setTravelSpeed(old);
		pilot.stop();
	}
	
	private void dokretZLinii(float stopnie, KierunekSensora kierunekSkretu)
	{
		int mnoznik = (kierunekSkretu == KierunekSensora.SENSOR_PRAWO) ? -1
				: 1;
		pilot.steer(100 * mnoznik, stopnie * mnoznik, true);
		while (dzialo.czyWidziCzarny() && pilot.isMoving())
			;
		pilot.stop();
	}
	
	private void dokretDoBokuLinii(float maxStopnie, KierunekSensora kierunekSkretu)
	{
		if (!dzialo.czyWidziCzarny())
		{
			dokretDoLinii(maxStopnie, kierunekSkretu);
			if (dzialo.czyWidziCzarny())
				skretZLinii(8, kierunekSkretu.przeciwny());
		}
	}
	
	private void skretDoBokuLinii(float maxStopnie, KierunekSensora kierunekSkretu)
	{
		if (!dzialo.czyWidziCzarny())
		{
			skretDoLinii(maxStopnie, kierunekSkretu);
			if (dzialo.czyWidziCzarny())
				skretZLinii(8, kierunekSkretu.przeciwny());
		}
	}
	
	private void skretDoLinii(float maxStopnie, KierunekSensora kierunekSkretu)
	{
		int mnoznik = (kierunekSkretu == KierunekSensora.SENSOR_PRAWO) ? -1
				: 1;
		//double old = pilot.getRotateSpeed();
		//pilot.setRotateSpeed(40);
		pilot.rotate(maxStopnie * mnoznik, true);
		while (!dzialo.czyWidziCzarny() && pilot.isMoving())
			;
		//pilot.setRotateSpeed(old);
		pilot.stop();
	}
	
	private void skretZLinii(float maxStopnie, KierunekSensora kierunekSkretu)
	{
		int mnoznik = (kierunekSkretu == KierunekSensora.SENSOR_PRAWO) ? -1
				: 1;
		double old = pilot.getRotateSpeed();
		pilot.setRotateSpeed(30);
	
		pilot.rotate(maxStopnie * mnoznik, true);
		while (dzialo.czyWidziCzarny() && pilot.isMoving())
			;
		pilot.setRotateSpeed(old);
		pilot.stop();
	}
	
	private void skret(float stopnie, KierunekSensora kierunekSkretu)
	{
		int mnoznik = (kierunekSkretu == KierunekSensora.SENSOR_PRAWO) ? -1
				: 1;
		dzialo.ustaw(kierunekSkretu);
		pilot.rotate(stopnie * mnoznik);
	}
	
	private void prostoDoKrawedzi(float dist)
	{
		pilot.travel(dist, true);
		while (!dzialo.czyWidziCzarny() && pilot.isMoving())
			;
		pilot.stop();
	}
	
	private void prostoZKrawedzi(float distKrawedzMaz, float distZaKrawedzia)
	{
		pilot.travel(distKrawedzMaz, true);
		while (dzialo.czyWidziCzarny() && pilot.isMoving())
			;
		pilot.travel(distZaKrawedzia);
		pilot.stop();
	}
	
	private void prostoPrzyKrawedzi(float dist)
	{
		updateLineDriver();
		lineDriver.reset(dist);
		lineDriver.drive();
		pilot.stop();
	}
	
	private void updateLineDriver()
	{
		if (dzialo.getKierunekSensora() == KierunekSensora.SENSOR_LEWO)
			lineDriver.lineOnLeft();
		else
			lineDriver.lineOnRight();
	}
}
