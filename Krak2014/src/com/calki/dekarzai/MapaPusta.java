package com.calki.dekarzai;

public class MapaPusta extends Mapa {
	public MapaPusta()
	{
		super();
		for (int x = 1; x < Plansza.ROZMIAR_PLANSZY_X - 1; ++x) {
			for (int y = 1; y < Plansza.ROZMIAR_PLANSZY_Y - 1; ++y) {
				dodajZnane(new Punkt(x, y));
			}
		}
	}
	@Override
	public void update(Kierowca kierowca)
	{
	}
}
