package com.calki.dekarzai;

import java.util.ArrayList;

public class TrasowyKierowca extends BazowyKierowca {
	protected int pozycjaWTrasie;
	ArrayList<Polecenie> trasa;
	/**
	 * kierowca ktory przejedzie podana mu trase
	 * @param trasa
	 */
	public TrasowyKierowca(Polecenie[] trasa, Kierunek ustawieniePoczatkowe, Punkt pozycjaPoczatkowa)
	{
		super();
		pozycjaWTrasie = 0;
		this.docelowePolozenie = pozycjaPoczatkowa;
		this.trasa = new ArrayList<Polecenie>();
		for (int i = 0; i < trasa.length; ++i)
		{
			this.trasa.add(trasa[i]);
		}
		this.kierunek = ustawieniePoczatkowe;
	}
	
	@Override
	public void nastepnyRuch()
	{
		if (pozycjaWTrasie >= this.trasa.size())
			this.polecenie = Polecenie.ZAKONCZ_JAZDE;
		this.polecenie = this.trasa.get(pozycjaWTrasie);
		if (this.polecenie != Polecenie.ZAKONCZ_JAZDE)
		{
			this.kierunek = this.kierunek.poPoleceniu(polecenie);
			
			Punkt next = Plansza.getNastepnyPunktWKierunku(this.docelowePolozenie, this.kierunek);
			if (next == null)
			{
				DebugUtils.printSystemError("FAIL, PODANA ZLA TRASA!!!");
			}
			this.aktualnePolozenie = this.docelowePolozenie;
			this.docelowePolozenie = next;
			pozycjaWTrasie++;
		}
		super.nastepnyRuch();
	}
}
