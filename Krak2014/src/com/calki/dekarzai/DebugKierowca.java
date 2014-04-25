package com.calki.dekarzai;

/*
 * DEKORATOR
 */
public class DebugKierowca implements Kierowca {

	private Kierowca kierowca;
	
	public DebugKierowca(Kierowca kierowca) {
		this.kierowca = kierowca;
	}

	@Override
	public void nastepnyRuch() {
		this.kierowca.nastepnyRuch();
		System.out.println("Poz: " + getAktualnePolozenie().x + " "+ getAktualnePolozenie().y);
		System.out.println(getPolecenie());
	}

	@Override
	public Polecenie getPolecenie() {
		return this.kierowca.getPolecenie();
	}

	@Override
	public Punkt getAktualnePolozenie() {
		return this.kierowca.getAktualnePolozenie();
	}

	@Override
	public Punkt getDocelowePolozenie() {
		return this.kierowca.getDocelowePolozenie();
	}

	@Override
	public Kierunek getAbsolutnyKierunekRuchu() {
		return this.kierowca.getAbsolutnyKierunekRuchu();
	}
}
