package com.calki.dekarzai;

import com.calki.util.SoundUtil;

public class MapaUltra extends Mapa {
	final int jednaOdlegloscDoKomina = 30;
	final int dwieOdleglosciDoKomina = 55;
	final int trzyOdleglosciDoKomina = 80;
	
	private Dzialo dzialo;

	public MapaUltra(Dzialo dzialo) {
		this.dzialo = dzialo;
	}

	private int odczytajOdlegloscUltra() {
		float zebranyOdczyt = dzialo.getDist();
		
		int iloscPolDoKomina = 99;

		if (zebranyOdczyt < jednaOdlegloscDoKomina) {
			iloscPolDoKomina = 1;
		} else if (zebranyOdczyt < dwieOdleglosciDoKomina) {
			iloscPolDoKomina = 2;
		} else if (zebranyOdczyt < trzyOdleglosciDoKomina) {
			iloscPolDoKomina = 3;
		} else {
			iloscPolDoKomina = 99;
		}

		return iloscPolDoKomina;
	}
	
	public void czyBliskoJestJakisKominDzwiek(){
		int odczyt = odczytajOdlegloscUltra();
		if(odczyt < jednaOdlegloscDoKomina){
			SoundUtil.dzwiek();
		} else if(odczyt < dwieOdleglosciDoKomina){
			SoundUtil.podwojnyDzwiek();
		}
	}
		
	@Override
	public void update(Kierowca kierowca) {
    	Punkt poz = kierowca.getDocelowePolozenie();
    	int iloscPolDoKomina = odczytajOdlegloscUltra();
		if (iloscPolDoKomina != 99) {
			for (int i = 0; i < iloscPolDoKomina; i++) {
				poz = Plansza.getNastepnyPunktWKierunku(poz, dzialo.getAbsulutnyKierunekSensora(kierowca.getAbsolutnyKierunekRuchu()));
				if (poz == null)
					break;
				if (!Plansza.jestNaKrawedzi(poz))
					dodajZnane(poz);
			}
			if (poz != null && !Plansza.jestNaKrawedzi(poz))
				dodajKomin(poz);
		} else {
			for (int i = 0; i < 3; i++) {
				poz = Plansza.getNastepnyPunktWKierunku(poz, dzialo.getAbsulutnyKierunekSensora(kierowca.getAbsolutnyKierunekRuchu()));
				if (poz == null)
					break;
				if (!Plansza.jestNaKrawedzi(poz)) {
					dodajZnane(poz);
				}
			}
		}
    }
}
