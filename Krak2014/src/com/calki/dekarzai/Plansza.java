package com.calki.dekarzai;

import java.util.*;

public class Plansza {

    private RodzajPola[][] pola;

    public Plansza(RodzajPola[][] nowePola) {
        pola = nowePola;
    }

    public RodzajPola[][] getPola() {
        return pola;
    }

    public RodzajPola getPole(int x, int y) {
        return pola[x][y];
    }

    public void setPole(int x, int y, RodzajPola val) {
        pola[x][y] = val;
    }

    public void reset() {
        for (int i = 0; i < ROZMIAR_PLANSZY_X; ++i) {
            for (int j = 0; j < ROZMIAR_PLANSZY_Y; ++j) {
                if (pola[i][j] == RodzajPola.POLE_ODWIEDZONE) {
                    pola[i][j] = RodzajPola.POLE_NIEODWIEDZONE;
                }
            }
        }
    }
    
    public void odwiedz(int x, int y) {
        if (pola[x][y] == RodzajPola.POLE_NIEODWIEDZONE) {
            pola[x][y] = RodzajPola.POLE_ODWIEDZONE;
        }
    }

    public static final int MIN_LICZBA_KOMINOW = 4;
    public static final int MAX_LICZBA_KOMINOW = 12;
    public static final int ROZMIAR_DACHU = 5;
    public static final int ROZMIAR_DACHU_X = ROZMIAR_DACHU;
    public static final int ROZMIAR_DACHU_Y = ROZMIAR_DACHU;
    public static final int LICZBA_POL_DACHU = ROZMIAR_DACHU_X * ROZMIAR_DACHU_Y;
    public static final int ROZMIAR_KRAWEDZI = 1;
    public static final int ROZMIAR_PLANSZY_X = ROZMIAR_DACHU_X + ROZMIAR_KRAWEDZI * 2;
    public static final int ROZMIAR_PLANSZY_Y = ROZMIAR_DACHU_Y + ROZMIAR_KRAWEDZI * 2;

    public static Plansza generujLosowa() {
        Random r = new Random();
        int liczbaKominow = 1 + MIN_LICZBA_KOMINOW + r.nextInt(1 + MAX_LICZBA_KOMINOW - MIN_LICZBA_KOMINOW);
        RodzajPola[][] nowePola;
        nowePola = new RodzajPola[ROZMIAR_PLANSZY_X][ROZMIAR_PLANSZY_Y];
        for (int i = 0; i < ROZMIAR_PLANSZY_X; ++i) {
            for (int j = 0; j < ROZMIAR_PLANSZY_Y; ++j) {
                if (jestNaKrawedzi(i, j)) {
                    nowePola[i][j] = RodzajPola.POLE_KRAWEDZ;
                } else {
                    nowePola[i][j] = RodzajPola.POLE_NIEODWIEDZONE;
                }
            }
        }

        ArrayList<Punkt> kominy = new ArrayList<>();
        while (kominy.size() < liczbaKominow) {
            Punkt p = get2DAddress(r.nextInt(ROZMIAR_PLANSZY_X * ROZMIAR_PLANSZY_Y));
            if (!jestNaKrawedzi(p) && !kominy.contains(p)) {
                kominy.add(p);
            }
        }

        for (Punkt p : kominy) {
            nowePola[p.x][p.y] = RodzajPola.POLE_KOMIN;
        }

        return new Plansza(nowePola);
    }

    public static boolean jestNaKrawedzi(int x, int y) {
        return x == 0 || y == 0 || x == ROZMIAR_PLANSZY_X - 1 || y == ROZMIAR_PLANSZY_Y - 1;
    }

    public static boolean jestNaBrzeguDachu(int x, int y) {
        return x == 1 || y == 1 || x == ROZMIAR_PLANSZY_X - 2 || y == ROZMIAR_PLANSZY_Y - 2;
    }

    public static boolean jestNaKrawedzi(Punkt p) {
        return jestNaKrawedzi(p.x, p.y);
    }

    public static boolean jestNaBrzeguDachu(Punkt p) {
        return jestNaBrzeguDachu(p.x, p.y);
    }

    public static Punkt get2DAddress(int x) {
        Integer addrX = x / ROZMIAR_PLANSZY_X;
        Integer addrY = x % ROZMIAR_PLANSZY_Y;
        return new Punkt(addrX, addrY);
    }

    public static boolean checkAddress(Punkt p) {
        return checkAddress(p.x, p.y);
    }

    public static boolean checkAddress(int x, int y) {
        return x < ROZMIAR_PLANSZY_X && y < ROZMIAR_PLANSZY_Y && x >= 0 && y >= 0;
    }
    
    public static boolean jestNaRoguKrawedzi(Punkt p)
    {
    	int ret = 0;
    	ret += (p.x == 0) ? 1 : 0;
    	ret += (p.y == 0) ? 1 : 0;
    	ret += (p.x == ROZMIAR_PLANSZY_X - 1) ? 1 : 0;
    	ret += (p.y == ROZMIAR_PLANSZY_Y - 1) ? 1 : 0;
    	return ret == 2;
    }
    
    public static Punkt[] getSasiedniePunkty(Punkt punkt) {
        Punkt[] punkty = new Punkt[4];
        if (punkt.x != 0) {
            punkty[Kierunek.KIERUNEK_LEWO.toInt()] = new Punkt(punkt.x - 1, punkt.y);
        }
        if (punkt.y != 0) {
            punkty[Kierunek.KIERUNEK_GORA.toInt()] = new Punkt(punkt.x, punkt.y - 1);
        }

        if (punkt.x != Plansza.ROZMIAR_PLANSZY_X - 1) {
            punkty[Kierunek.KIERUNEK_PRAWO.toInt()] = new Punkt(punkt.x + 1, punkt.y);
        }
        if (punkt.y != Plansza.ROZMIAR_PLANSZY_Y - 1) {
            punkty[Kierunek.KIERUNEK_DOL.toInt()] = new Punkt(punkt.x, punkt.y + 1);
        }
        return punkty;
    }
    
    public static Kierunek getKierunekDoKrawedziDachu(Punkt punkt)
    {
    	if (punkt.x == 0)
    	{
    		if (punkt.y == 0 || punkt.y == ROZMIAR_PLANSZY_Y - 1)
    			return null;
    		return Kierunek.KIERUNEK_PRAWO;
    	}
    	else if (punkt.x == ROZMIAR_PLANSZY_X - 1)
    	{
    		if (punkt.y == 0 || punkt.y == ROZMIAR_PLANSZY_Y - 1)
    			return null;
    		return Kierunek.KIERUNEK_LEWO;
    	}
    	else
    	{
    		if (punkt.y == 0)
    			return Kierunek.KIERUNEK_DOL;
    		else if (punkt.y == ROZMIAR_PLANSZY_Y - 1)
    			return Kierunek.KIERUNEK_GORA;
    		return null;
    	}
    }
    
    public static Punkt getNastepnyPunktWKierunku(Punkt punkt, Kierunek kierunek) {
        Punkt ret;
        switch (kierunek) {
            case KIERUNEK_DOL:
                ret = new Punkt(punkt.x, punkt.y + 1);
                break;
            case KIERUNEK_GORA:
                ret = new Punkt(punkt.x, punkt.y - 1);
                break;
            case KIERUNEK_LEWO:
                ret = new Punkt(punkt.x - 1, punkt.y);
                break;
            case KIERUNEK_PRAWO:
                ret = new Punkt(punkt.x + 1, punkt.y);
                break;
            default:
                return null;
        }
        if (!Plansza.checkAddress(ret)) {
            return null;
        }
        return ret;
    }
}
