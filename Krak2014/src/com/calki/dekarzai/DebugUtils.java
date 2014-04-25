package com.calki.dekarzai;

import lejos.nxt.LCD;

public class DebugUtils {
	public static void wypiszPolozenie(Kierowca kierowca) {
		System.out.println("X:" + kierowca.getAktualnePolozenie().x);
		System.out.println("Y:" + kierowca.getAktualnePolozenie().y);
	}
	
	public static void wypiszMape(Mapa mapa) {
		boolean[][] polaZakazane, polaZnane;
		polaZnane = mapa.getPolaZnane();
		polaZakazane = mapa.getPolaZakazane();
		LCD.clear();
		for (int x = 1; x < Plansza.ROZMIAR_PLANSZY_X-1; ++x) {
			for (int y = 1; y < Plansza.ROZMIAR_PLANSZY_Y-1; ++y) {
				if (polaZnane[y][x] == true) {
					System.out.print('1');
				} else {
					System.out.print('0');
				}
				if (polaZakazane[y][x] == false) {
					System.out.print(".");
				} else {
					System.out.print("K");
				}
			}
			System.out.println();
		}
	}
	
	public static void printSystemError(String error)
	{
		System.out.println(error);
		while(true)
			;
	}
}
