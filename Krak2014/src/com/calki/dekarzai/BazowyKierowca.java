package com.calki.dekarzai;

public class BazowyKierowca implements Kierowca {

	protected Punkt aktualnePolozenie;
	protected Punkt docelowePolozenie;
	protected Polecenie polecenie;
	protected Kierunek kierunek;
    
    public BazowyKierowca() {
    	aktualnePolozenie = null;
    	docelowePolozenie = new Punkt(Plansza.ROZMIAR_PLANSZY_X - 1, Plansza.ROZMIAR_PLANSZY_Y - 1);
    }

	@Override
	public void nastepnyRuch() {
	}

	@Override
	public Polecenie getPolecenie() {
		// TODO Auto-generated method stub
		return polecenie;
	}

	@Override
	public Punkt getAktualnePolozenie() {
		// TODO Auto-generated method stub
		return aktualnePolozenie;
	}

	@Override
	public Punkt getDocelowePolozenie() {
		// TODO Auto-generated method stub
		return docelowePolozenie;
	}

	@Override
	public Kierunek getAbsolutnyKierunekRuchu() {
		// TODO Auto-generated method stub
		return kierunek;
	}
}
