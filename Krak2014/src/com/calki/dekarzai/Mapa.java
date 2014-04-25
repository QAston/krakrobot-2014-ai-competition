package com.calki.dekarzai;

public abstract class Mapa {
	protected int liczbaWykrytychKominow = 0;
    protected int liczbaZnanychPol = 0;
    protected int liczbaZaliczonychPol = 0;
    protected boolean zaktualizowanoPozycjeKominow = false;
    protected boolean[][] polaZakazane = new boolean[Plansza.ROZMIAR_PLANSZY_X][Plansza.ROZMIAR_PLANSZY_Y];
    protected boolean[][] polaZnane = new boolean[Plansza.ROZMIAR_PLANSZY_X][Plansza.ROZMIAR_PLANSZY_Y];
    
    public boolean czyPoleZakazane(Punkt p)
    {
    	return polaZakazane[p.x][p.y];
    }
    
    public boolean czyPoleZakazaneINaPlanszy(Punkt p)
    {
    	return Plansza.checkAddress(p) && polaZakazane[p.x][p.y];
    }
    
    public boolean czyPoleZnane(Punkt p)
    {
    	return polaZnane[p.x][p.y];
    }
    
    public void dodajKomin(Punkt p)
    {
    	if (czyPoleZakazane(p))
    		return;
    	dodajZnane(p);
    	polaZakazane[p.x][p.y] = true;
    	liczbaWykrytychKominow++;
    	zaktualizowanoPozycjeKominow = true;
    }
    
    public void dodajZnane(Punkt p)
    {
    	if (czyPoleZnane(p))
    		return;
    	polaZnane[p.x][p.y] = true;
    	liczbaZnanychPol++;
    }
    
    public void dodajZaliczone(Punkt p)
    {
    	if (czyPoleZakazane(p))
    		throw new RuntimeException("Wjazd na pole zakazane!");
    	dodajZnane(p);
    	polaZakazane[p.x][p.y] = true;
    	liczbaZaliczonychPol++;
    }
    
    public int getLiczbaKominow()
    {
    	return liczbaWykrytychKominow;
    }
    
    public int getLiczbaZaliczonychPol()
    {
    	return liczbaZaliczonychPol;
    }
    
    public boolean[][] getPolaZakazane()
    {
    	return polaZakazane;
    }
    
    public boolean[][] getPolaZnane()
    {
    	return polaZnane;
    }
    
    public int getLiczbaZakazanychSasiednichPol(Punkt punkt) {
        int lZakazanychSasiadow = 0;
        if (czyPoleZakazane(new Punkt(punkt.x + 1, punkt.y))) {
            ++lZakazanychSasiadow;
        }
        if (czyPoleZakazane(new Punkt(punkt.x - 1, punkt.y))) {
            ++lZakazanychSasiadow;
        }
        if (czyPoleZakazane(new Punkt(punkt.x, punkt.y + 1))) {
            ++lZakazanychSasiadow;
        }
        if (czyPoleZakazane(new Punkt(punkt.x, punkt.y - 1))) {
            ++lZakazanychSasiadow;
        }
        return lZakazanychSasiadow;
    }
    
    public abstract void update(Kierowca kierowca);
}
