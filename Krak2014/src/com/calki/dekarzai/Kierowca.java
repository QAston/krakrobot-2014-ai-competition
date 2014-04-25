package com.calki.dekarzai;

public interface Kierowca {
	void nastepnyRuch();
	Polecenie getPolecenie();
	Punkt getAktualnePolozenie();
	Punkt getDocelowePolozenie();
	Kierunek getAbsolutnyKierunekRuchu();
}
