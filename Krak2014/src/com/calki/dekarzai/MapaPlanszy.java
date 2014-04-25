package com.calki.dekarzai;

import java.util.ArrayList;
import java.util.List;

public class MapaPlanszy extends Mapa {
	
	private Plansza plansza;
	
	public MapaPlanszy(Plansza plansza)
	{
		this.plansza = plansza;
	}
	
	@Override
	public void update(Kierowca kierowca) {
        List<Kierunek> kierunki = new ArrayList<>();
        switch (kierowca.getAbsolutnyKierunekRuchu()) {
            case KIERUNEK_GORA:
            case KIERUNEK_DOL:
                kierunki.add(kierowca.getAbsolutnyKierunekRuchu());
                kierunki.add(Kierunek.KIERUNEK_LEWO);
                kierunki.add(Kierunek.KIERUNEK_PRAWO);
                break;
            case KIERUNEK_LEWO:
            case KIERUNEK_PRAWO:
                kierunki.add(kierowca.getAbsolutnyKierunekRuchu());
                kierunki.add(Kierunek.KIERUNEK_GORA);
                kierunki.add(Kierunek.KIERUNEK_DOL);
                break;
        }
        for (Kierunek kierunek : kierunki) {
            Punkt poz = kierowca.getDocelowePolozenie();
            while (true) {
                poz = Plansza.getNastepnyPunktWKierunku(poz, kierunek);
                if (poz == null) {
                    break;
                }
                if (!Plansza.jestNaKrawedzi(poz)) {
                	dodajZnane(poz);
                }
                // nie widac nic za kominem
                if (plansza.getPole(poz.x, poz.y) == RodzajPola.POLE_KOMIN)
                {
                	dodajKomin(poz);
                    break;
                }
            }
        }
	}
}
