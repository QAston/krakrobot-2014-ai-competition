package com.calki.dekarzai;

import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;

public class StaryRuchacz implements Ruchacz {

	private Dzialo dzialo;
	private Mapa mapa;
	private Kierowca kierowca;
	protected DifferentialPilot pilot;
	protected static final int TachoCount = 485,
			TachoCountNaDachu = 240, PREDKOSC_DOJAZDOWA = 10,
			PREDKOSC_STANDARDOWA = 55, ROTATE_NORMAL = 55, ROTATE_DOJAZDOWA = 40;
	
	public StaryRuchacz(Dzialo dzialo, Kierowca kierowca, Mapa mapa)
	{
		this.dzialo = dzialo;
		this.kierowca = kierowca;
		this.mapa = mapa;
		pilot = new DifferentialPilot(5.4f, 10.5f, Motor.A, Motor.B, false);
		pilot.setRotateSpeed(PREDKOSC_STANDARDOWA);
		pilot.setTravelSpeed(PREDKOSC_STANDARDOWA);
		pilot.setAcceleration(30);
	}

	@Override
	public void jedzDoPrzodu() {
		jedz_do_przedu(false);
	}

	@Override
	public void jedzDoTylu() {
		jedz_do_tylu();
	}

	@Override
	public void jedzWPrawo() {
		jedz_do_przedu(skrec(KierunekSensora.SENSOR_PRAWO));
	}

	@Override
	public void jedzWLewo() {
		jedz_do_przedu(skrec(KierunekSensora.SENSOR_LEWO));
	}
	
	protected void jedzDoPrzoduPoDachu(boolean blisko) {

		pilot.setTravelSpeed(PREDKOSC_DOJAZDOWA);
		if (!blisko)
			jedzObokCzarnego(TachoCountNaDachu - 40);
		if (dzialo.getKierunekSensora() == KierunekSensora.SENSOR_PROSTO)
			dzialo.ustaw(KierunekSensora.SENSOR_PRAWO);
		KierunekSensora staryKierunekSensora = dzialo.getKierunekSensora();
		int mnoznik = (dzialo.getKierunekSensora() == KierunekSensora.SENSOR_PRAWO) ? 1
				: -1;
		jedzObokCzarnego(TachoCountNaDachu);
		if (dzialo.czyWidziCzarny())
			pilot.rotate(mnoznik * 7);
		dzialo.ustaw(KierunekSensora.SENSOR_PROSTO);
		if (!Plansza.jestNaKrawedzi(kierowca.getDocelowePolozenie())) {
			pilot.setTravelSpeed(PREDKOSC_DOJAZDOWA);
			pilot.forward();
			while (!dzialo.czyWidziCzarny())
				;
			pilot.stop();
		} else
			pilot.travel(5);
		
		mapa.update(kierowca);
		dzialo.ustaw(staryKierunekSensora.przeciwny());
		mapa.update(kierowca);
		dzialo.ustaw(staryKierunekSensora);
		mapa.update(kierowca);
		pilot.setTravelSpeed(PREDKOSC_STANDARDOWA);
	}
	
	protected void jedzObokCzarnego(int ile) {
		Motor.B.resetTachoCount();
		Motor.A.resetTachoCount();
		int mnoznik = (dzialo.getKierunekSensora() == KierunekSensora.SENSOR_PRAWO) ? -1
				: 1;
		do {
			if (dzialo.czyWidziCzarny()) {
				if (Motor.B.getTachoCount() > ile
						&& Motor.A.getTachoCount() > ile)
					pilot.stop();
				else {
					pilot.steer(-15 * mnoznik, -3 * mnoznik, true);
				}
			} else {

				if (Motor.B.getTachoCount() > ile
						&& Motor.A.getTachoCount() > ile)
					pilot.stop();
				else {
					pilot.steer(10 * mnoznik, 3 * mnoznik, true);
				}
			}
		} while (pilot.isMoving());
	}

	protected void jedzDoPrzoduPoKrawedzi() {
		Kierunek kierunekDoPlanszy = Plansza
				.getKierunekDoKrawedziDachu(kierowca.getDocelowePolozenie());
		if (kierunekDoPlanszy != null) {
			if (kierowca.getAbsolutnyKierunekRuchu().lewo() == kierunekDoPlanszy) {
				dzialo.ustaw(KierunekSensora.SENSOR_LEWO);
			} else if (kierowca.getAbsolutnyKierunekRuchu().prawo() == kierunekDoPlanszy) {
				dzialo.ustaw(KierunekSensora.SENSOR_PRAWO);
			} else {
				// cos poszlo nie tak z poleceniami jazdy
				assert (false);
				return;
			}
		}
		
		if (Plansza.getKierunekDoKrawedziDachu(kierowca.getAktualnePolozenie()) == null) {
			//pilot.travel(14, true);
			//while (SENSOR_KOLOR.getRawLightValue() > CZARNY && pilot.isMoving())
			//	;
			//pilot.stop();
			//pilot.steer(-10 * mnoznik, -3 * mnoznik, true);
			jedzObokCzarnego(TachoCount / 2);
		} else if (Plansza.getKierunekDoKrawedziDachu(kierowca.getDocelowePolozenie()) == null) {
			jedzObokCzarnego(TachoCount / 2);
			pilot.travel(14, true);
			while (!dzialo.czyWidziCzarny() && pilot.isMoving())
				;
			pilot.stop();
		} else {
			jedzObokCzarnego(TachoCount);
		}
		mapa.update(kierowca);
	}

	protected void jedzDoPrzodu(boolean blisko) {
		if (Plansza.jestNaKrawedzi(kierowca.getAktualnePolozenie())
				&& Plansza.jestNaKrawedzi(kierowca.getDocelowePolozenie())) {
			jedzDoPrzoduPoKrawedzi();
		} else {
			jedzDoPrzoduPoDachu(blisko);
		}
	}

	protected void jedz_do_przedu(boolean blisko) {
		jedzDoPrzodu(blisko);
	}

	protected boolean skrec(KierunekSensora kierunekSkretu) {
		int mnoznik = (kierunekSkretu == KierunekSensora.SENSOR_PRAWO) ? -1
				: 1;
		boolean blisko = false;
		if (Plansza.jestNaKrawedzi(kierowca.getAktualnePolozenie())
				&& Plansza.jestNaKrawedzi(kierowca.getDocelowePolozenie())) {
			dzialo.ustaw(kierunekSkretu);
			if (dzialo.czyWidziCzarny())
				jedzDoPrzodu(true);
			pilot.rotate(95 * mnoznik, true);
			while (!dzialo.czyWidziCzarny() && pilot.isMoving())
				;
			pilot.stop();
		} else if (!Plansza.jestNaKrawedzi(kierowca.getDocelowePolozenie()) && dzialo.getKierunekSensora() == kierunekSkretu) {
			blisko = true;
			dzialo.ustaw(kierunekSkretu);
			if (dzialo.czyWidziCzarny()) {
				pilot.rotate(90 * mnoznik);
			} else {
				pilot.setRotateSpeed(ROTATE_DOJAZDOWA);
				pilot.rotate(5 * mnoznik, true);
				double angle;
				do {
					if (dzialo.czyWidziCzarny()) {
						angle = 90 * mnoznik;
						break;
					}
					if (!pilot.isMoving()) {
						angle = 80 * mnoznik;
						break;
					}
				} while (true);
				pilot.setRotateSpeed(ROTATE_NORMAL);
				pilot.rotate(angle);
			}
		} else {
			dzialo.ustaw(kierunekSkretu);
			pilot.rotate(90 * mnoznik, true);
			while (!dzialo.czyWidziCzarny() && pilot.isMoving())
				;
			pilot.stop();
		}
		return blisko;
	}

	protected void jedz_do_tylu() {
		if (Plansza.jestNaBrzeguDachu(kierowca.getAktualnePolozenie())) {
			while (!dzialo.czyWidziCzarny()) {
				pilot.backward();
			}
			pilot.rotate(180);
			jedzDoPrzodu(false);
		} else {
			dzialo.ustaw(dzialo.getKierunekSensora().przeciwny());
			if (dzialo.getKierunekSensora() == KierunekSensora.SENSOR_LEWO) {
				pilot.rotate(90);
				pilot.rotate(90, true);
				while (!dzialo.czyWidziCzarny() && pilot.isMoving())
					;
				pilot.stop();
			} else {
				pilot.rotate(-90);
				pilot.rotate(-90, true);
				while (!dzialo.czyWidziCzarny() && pilot.isMoving())
					;
				pilot.stop();
			}
		}
	}
}
