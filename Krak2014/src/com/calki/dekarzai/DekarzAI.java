package com.calki.dekarzai;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.util.Stopwatch;

import com.calki.util.*;

public class DekarzAI implements AI {

	@Override
	public void run() {
		/*
		
		Kierowca kierowca = new Robot(mapa);
		*/
		Button.waitForAnyPress();
		Dzialo dzialo = new Dzialo(new ColorSensorReader(SensorPort.S2), new UltraSensorReader(SensorPort.S1));
		Mapa mapa = new MapaUltra(dzialo);
		/* TESTY - trzeba wykonywaæ przy ka¿dej wiêkszej poprawce, aby upewniæ siê ¿e nic siê nie zepsu³o!
		Polecenie[] trasa = {Polecenie.JEDZ_PROSTO, 
		                     Polecenie.JEDZ_PROSTO, 
		                     Polecenie.JEDZ_W_LEWO, 
		                     Polecenie.JEDZ_W_LEWO, 
		                     Polecenie.JEDZ_W_LEWO, 
		                     Polecenie.JEDZ_W_LEWO, 
		                     Polecenie.ZAKONCZ_JAZDE};
		                     
		Polecenie[] trasa = {Polecenie.JEDZ_PROSTO, 
		                     Polecenie.JEDZ_PROSTO,
		                     Polecenie.JEDZ_PROSTO, 
		                     Polecenie.JEDZ_PROSTO,
		                     Polecenie.JEDZ_PROSTO, 
		                     Polecenie.JEDZ_PROSTO, 
		                     Polecenie.JEDZ_W_LEWO, 
		                     Polecenie.JEDZ_W_LEWO, 
		                     Polecenie.JEDZ_W_LEWO, 
		                     Polecenie.JEDZ_W_LEWO, 
		                     Polecenie.ZAKONCZ_JAZDE};
		Polecenie[] trasa = {Polecenie.JEDZ_PROSTO, 
		                     Polecenie.JEDZ_PROSTO,
		                     Polecenie.JEDZ_W_LEWO, 
		                     Polecenie.JEDZ_PROSTO, 
		                     Polecenie.JEDZ_PROSTO,
		                     Polecenie.JEDZ_PROSTO, 
		                     Polecenie.JEDZ_PROSTO, 
		                     Polecenie.JEDZ_PROSTO, 
		                     Polecenie.JEDZ_W_LEWO, 
		                     Polecenie.JEDZ_W_LEWO, 
		                     Polecenie.JEDZ_PROSTO,
		                     Polecenie.JEDZ_PROSTO, 
		                     Polecenie.JEDZ_PROSTO,
		                     Polecenie.JEDZ_PROSTO, 
		                     Polecenie.JEDZ_PROSTO, 
		                     Polecenie.ZAKONCZ_JAZDE};
		 */
		/*Polecenie[] trasa = {Polecenie.JEDZ_PROSTO, 
		                     Polecenie.JEDZ_PROSTO,
		                     Polecenie.JEDZ_W_LEWO,  
		                     Polecenie.JEDZ_W_PRAWO,
		                     Polecenie.JEDZ_PROSTO, 
		                     Polecenie.JEDZ_W_LEWO,
		                     Polecenie.ZAKONCZ_JAZDE};*/

		/*Polecenie[] trasa = {Polecenie.JEDZ_PROSTO, 
        Polecenie.JEDZ_PROSTO,
        Polecenie.JEDZ_W_LEWO,  
        Polecenie.JEDZ_PROSTO,
        Polecenie.JEDZ_W_PRAWO,
        Polecenie.JEDZ_PROSTO, 
        Polecenie.JEDZ_PROSTO, 
        Polecenie.JEDZ_W_LEWO,
        Polecenie.ZAKONCZ_JAZDE};*/
		Polecenie[] trasa = {Polecenie.JEDZ_PROSTO, 
        Polecenie.JEDZ_PROSTO,
        Polecenie.JEDZ_W_LEWO,
        Polecenie.JEDZ_W_PRAWO,
        Polecenie.JEDZ_W_LEWO,
        Polecenie.ZAKONCZ_JAZDE};

		Kierowca kierowca = 
				new TrasowyKierowca(trasa, Kierunek.KIERUNEK_GORA, 
				new Punkt(Plansza.ROZMIAR_PLANSZY_X-1, Plansza.ROZMIAR_PLANSZY_Y-1));
		
		Ruchacz ruchacz = new BetterRuchacz(dzialo, kierowca, mapa);
		
		Stopwatch zegar = new Stopwatch();
		zegar.reset();
		while (true) {
			if(zegar.elapsed() > 240000){
				SoundUtil.podwojnyDzwiek();
				return;
			}
			
			kierowca.nastepnyRuch();
			Polecenie polecenie = kierowca.getPolecenie();
			switch (polecenie) {
			case JEDZ_PROSTO:
				ruchacz.jedzDoPrzodu();
				break;
			case JEDZ_W_LEWO:
				ruchacz.jedzWLewo();
				break;
			case JEDZ_W_PRAWO:
				ruchacz.jedzWPrawo();
				break;
			case JEDZ_DO_TYLU:
				ruchacz.jedzDoTylu();
				break;
			case ZAKONCZ_JAZDE:
				SoundUtil.podwojnyDzwiek();
				Button.waitForAnyPress();
				return;
			}
		}
	}
}
