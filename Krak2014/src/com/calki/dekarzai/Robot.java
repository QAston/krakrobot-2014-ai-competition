package com.calki.dekarzai;

import java.util.*;

/**
 *
 * @author QAston <qaston@gmail.com>
 */
public class Robot implements Kierowca {

    private Kierunek kierunekJazdy;
    private Punkt pozycjaDocelowa;
    private Mapa mapa;
    private boolean[][] polaSlepeUliczki = new boolean[Plansza.ROZMIAR_PLANSZY_X][Plansza.ROZMIAR_PLANSZY_Y];
    private int[][] grupyPol = new int[Plansza.ROZMIAR_PLANSZY_X][Plansza.ROZMIAR_PLANSZY_Y];

    private int ocenaNajlepszejSciezki;
    private boolean zaktualizowanoPozycjeKominow = false;
    private StanRobota stanRobota = StanRobota.STAN_ANALIZA_PLANSZY;
    private ArrayList<Przystanek> droga = new ArrayList<>();
    private int drogaIndex;
    private boolean reset = true;

    public Robot(Mapa mapa) {

    	this.mapa = mapa;
    	pozycjaAktualna = null;
        pozycjaDocelowa = new Punkt(Plansza.ROZMIAR_PLANSZY_X - 1, Plansza.ROZMIAR_PLANSZY_Y - 1);
        kierunekJazdy = Kierunek.KIERUNEK_GORA;

        Punkt poz = new Punkt(pozycjaDocelowa.x, pozycjaDocelowa.y);
        for (int i = 0; i < Plansza.ROZMIAR_PLANSZY_Y - 1; ++i) {
            poz = Plansza.getNastepnyPunktWKierunku(poz, Kierunek.KIERUNEK_GORA);
            droga.add(new Przystanek(poz, Kierunek.KIERUNEK_GORA));
        }
        for (int i = 0; i < Plansza.ROZMIAR_PLANSZY_X - 1; ++i) {
            poz = Plansza.getNastepnyPunktWKierunku(poz, Kierunek.KIERUNEK_LEWO);
            droga.add(new Przystanek(poz, Kierunek.KIERUNEK_LEWO));
        }
        for (int i = 0; i < Plansza.ROZMIAR_PLANSZY_Y - 1; ++i) {
            poz = Plansza.getNastepnyPunktWKierunku(poz, Kierunek.KIERUNEK_DOL);
            droga.add(new Przystanek(poz, Kierunek.KIERUNEK_DOL));
        }
        for (int i = 0; i < Plansza.ROZMIAR_PLANSZY_X - 2; ++i) {
            poz = Plansza.getNastepnyPunktWKierunku(poz, Kierunek.KIERUNEK_PRAWO);
            droga.add(new Przystanek(poz, Kierunek.KIERUNEK_PRAWO));
        }
        drogaIndex = 0;
    }

    public Kierunek getKierunekJazdy() {
        return kierunekJazdy;
    }

    public int getX() {
        return pozycjaDocelowa.x;
    }

    public int getY() {
        return pozycjaDocelowa.y;
    }

    public List<Punkt> getTrasa() {
        List<Punkt> trasa = new ArrayList<>();
        int drogaSize = droga.size();
        for (int i = drogaIndex; i < drogaSize; ++i) {
            Przystanek p = droga.get(i);
            if (p != null) {
                trasa.add(p.miejsce);
            }
        }
        return trasa;
    }

    public boolean isReset() {
        return reset;
    }

    public Polecenie go() {

        reset = false;

        switch (stanRobota) {
            case STAN_KONIEC_PRZEJAZDU:
                return Polecenie.ZAKONCZ_JAZDE;
            default:
                break;
        }

        analizujPlansze();

        // przygotowania do wjazdu na nowe pole
        Kierunek poprzedniKierunekJazdy = kierunekJazdy;
        Punkt nextPos = null;
        if (!droga.isEmpty() && drogaIndex < droga.size()) {
            Przystanek przystanek = droga.get(drogaIndex++);

            if (przystanek == null) {
                stanRobota = StanRobota.STAN_KONIEC_PRZEJAZDU;
                return Polecenie.ZAKONCZ_JAZDE;
            } else {
                kierunekJazdy = przystanek.ostatniKierunek;
                nextPos = getNastepnaPozycja();
            }
        }

        switch (stanRobota) {
            case STAN_ANALIZA_PLANSZY:
                if (nextPos != null && !mapa.czyPoleZakazane(nextPos)) {
                    break;
                } else {
                    stanRobota = StanRobota.STAN_PRZEJAZD_PO_PLANSZY;
                    nextPos = null;
                    //nobreak;
                }
            case STAN_PRZEJAZD_PO_PLANSZY:
                // aktualizacja trasy: gdy jest koniec sciezki, pole zaplanowane jako puste okazalo sie zajete, odkrylismy nowe kominy podczas skretu wewnatrz trasy
                if ((nextPos == null && Plansza.jestNaKrawedzi(pozycjaDocelowa)) || mapa.czyPoleZakazane(nextPos) || (poprzedniKierunekJazdy != kierunekJazdy && zaktualizowanoPozycjeKominow)) {
                    zaktualizowanoPozycjeKominow = false;
                    droga.clear();
                    drogaIndex = 0;
                    ArrayList<Przystanek> nowaDroga = znajdzDrogeDoNieodwiedzonegoPola();
                    if (nowaDroga != null && !nowaDroga.isEmpty()) {
                        int drogaSize = nowaDroga.size();
                        for (int i = 1; i < drogaSize; ++i) {
                            droga.add(nowaDroga.get(i));
                        }

                        kierunekJazdy = droga.get(drogaIndex++).ostatniKierunek;
                        nextPos = getNastepnaPozycja();
                    } else {
                        stanRobota = StanRobota.STAN_KONIEC_PRZEJAZDU;
                        return Polecenie.ZAKONCZ_JAZDE;
                    }
                }
            default:
                break;
        }
        // oznacz, ze jestes na nowym polu
        pozycjaDocelowa = nextPos;
        
        if (!Plansza.jestNaKrawedzi(pozycjaDocelowa)) {
            mapa.dodajZaliczone(pozycjaDocelowa);
        }

        Polecenie polecenie = null;
        if (poprzedniKierunekJazdy == kierunekJazdy) {
            polecenie = Polecenie.JEDZ_PROSTO;
        } else if (poprzedniKierunekJazdy.przeciwny() == kierunekJazdy) {
            polecenie = Polecenie.JEDZ_DO_TYLU;
        } else if (poprzedniKierunekJazdy.lewo() == kierunekJazdy) {
            polecenie = Polecenie.JEDZ_W_LEWO;
        } else if (poprzedniKierunekJazdy.prawo() == kierunekJazdy) {
            polecenie = Polecenie.JEDZ_W_PRAWO;
        }

        return polecenie;
    }
    
    public void zakonczAnalizePlanszy()
    {
    	droga.clear();
    	stanRobota = StanRobota.STAN_PRZEJAZD_PO_PLANSZY;
    }

    private Punkt getNastepnaPozycja() {
        return Plansza.getNastepnyPunktWKierunku(pozycjaDocelowa, kierunekJazdy);
    }
    
    private static Przystanek[] getSasiedniePrzystanki(Punkt punkt) {
        Przystanek[] punkty = new Przystanek[4];
        if (punkt.x != 0) {
            punkty[Kierunek.KIERUNEK_LEWO.toInt()] = new Przystanek(new Punkt(punkt.x - 1, punkt.y), Kierunek.KIERUNEK_LEWO);
        }
        if (punkt.y != 0) {
            punkty[Kierunek.KIERUNEK_GORA.toInt()] = new Przystanek(new Punkt(punkt.x, punkt.y - 1), Kierunek.KIERUNEK_GORA);
        }

        if (punkt.x != Plansza.ROZMIAR_PLANSZY_X - 1) {
            punkty[Kierunek.KIERUNEK_PRAWO.toInt()] = new Przystanek(new Punkt(punkt.x + 1, punkt.y), Kierunek.KIERUNEK_PRAWO);
        }
        if (punkt.y != Plansza.ROZMIAR_PLANSZY_Y - 1) {
            punkty[Kierunek.KIERUNEK_DOL.toInt()] = new Przystanek(new Punkt(punkt.x, punkt.y + 1), Kierunek.KIERUNEK_DOL);
        }
        return punkty;
    }

    private int getLiczbaPozostalychPol(int liczbaPolDodatkowych) {
        return Plansza.LICZBA_POL_DACHU - mapa.getLiczbaZaliczonychPol() - mapa.getLiczbaKominow() - liczbaPolDodatkowych;
    }

    private float getPunktyZaPole() {
        return 150 / (36 - mapa.getLiczbaKominow());
    }

    private int getKaraZaNieukonczonyPrzejazd() {
        return 11;
    }

    private int getAktualnyBonusZaUkonczenie(int liczbaPolDodatkowych) {
        switch (getLiczbaPozostalychPol(liczbaPolDodatkowych)) {
            case 0:
                return 41;
            case 1:
                return 16;
            case 2:
                return 6;
            default:
                return 0;
        }
    }

    private boolean czyOplacaSieZablokowac(int liczbaPolBezWyjscia, int liczbaPolDodatkowych) {
        return (liczbaPolBezWyjscia * getPunktyZaPole() - getKaraZaNieukonczonyPrzejazd()) > getAktualnyBonusZaUkonczenie(liczbaPolDodatkowych);
    }

    private ArrayList<Przystanek> znajdzDrogeDoNieodwiedzonegoPola() {
        return znajdzDrogeDoNieodwiedzonegoPola(pozycjaDocelowa, kierunekJazdy, mapa.getPolaZakazane(), 0);
    }

    private static int[][] wyznaczGrupy(boolean[][] zabronione, List<List<Punkt>> list) {
        int nrGrupy = 1;
        int[][] grupy = new int[Plansza.ROZMIAR_PLANSZY_X][Plansza.ROZMIAR_PLANSZY_Y];
        for (int x = 1; x < Plansza.ROZMIAR_PLANSZY_X - 1; ++x) {
            for (int y = 1; y < Plansza.ROZMIAR_PLANSZY_Y - 1; ++y) {
                List<Punkt> grupa = wyznaczGrupe(x, y, nrGrupy, grupy, zabronione);
                if (grupa != null) {
                    ++nrGrupy;
                    if (list != null) {
                        list.add(grupa);
                    }
                }
            }
        }
        return grupy;
    }

    private boolean[][] wyznaczSlepeUliczki(boolean[][] zabronione) {
        boolean[][] slepeUliczki = new boolean[Plansza.ROZMIAR_PLANSZY_X][Plansza.ROZMIAR_PLANSZY_Y];
        for (int x = 1; x < Plansza.ROZMIAR_PLANSZY_X - 1; ++x) {
            for (int y = 1; y < Plansza.ROZMIAR_PLANSZY_Y - 1; ++y) {
                if (!zabronione[x][y] && !slepeUliczki[x][y]) {
                    getPolaSlepejUliczki(new Punkt(x, y), zabronione, slepeUliczki);
                }
            }
        }
        return slepeUliczki;
    }

    private static List<Punkt> wyznaczGrupe(int x, int y, int nrGrupy, int[][] grupy, boolean[][] zabronione) {
        if (grupy[x][y] == 0 && !Plansza.jestNaKrawedzi(x, y) && !zabronione[x][y]) {
            List<Punkt> grupa = new ArrayList<>();
            grupa.add(new Punkt(x, y));
            grupy[x][y] = nrGrupy;
            Punkt aktualny;
            int itr = 0;
            while (itr < grupa.size()) {
                aktualny = grupa.get(itr++);

                Punkt[] punkty = Plansza.getSasiedniePunkty(aktualny);
                for (Punkt next : punkty) {
                    if (grupy[next.x][next.y] == 0 && !Plansza.jestNaKrawedzi(next) && !zabronione[next.x][next.y]) {
                        grupy[next.x][next.y] = nrGrupy;
                        grupa.add(next);
                    }
                }
            }
            return grupa;
        }
        return null;
    }

    private static void zakazInnychGrup(int nrGrupy, boolean[][] zakazane, int[][] grupy) {
        for (int x = 1; x < Plansza.ROZMIAR_PLANSZY_X - 1; ++x) {
            for (int y = 1; y < Plansza.ROZMIAR_PLANSZY_Y - 1; ++y) {
                if (grupy[x][y] != nrGrupy) {
                    zakazane[x][y] = true;
                }
            }
        }
    }

    private static List<Punkt> wyznaczSlepaUliczke(int x, int y, Kierunek kierunek, boolean[][] slepeUliczki, boolean[][] zabronione) {
        if (!slepeUliczki[x][y] && !Plansza.jestNaKrawedzi(x, y) && !zabronione[x][y]) {

            boolean[][] noweSlepe = new boolean[Plansza.ROZMIAR_PLANSZY_X][Plansza.ROZMIAR_PLANSZY_Y];
            // tablica zakazanych pol do juz przeanalizowanych
            for (int i = 0; i < Plansza.ROZMIAR_PLANSZY_X; ++i) {
                System.arraycopy(slepeUliczki[i], 0, noweSlepe[i], 0, Plansza.ROZMIAR_PLANSZY_Y);
            }

            List<Punkt> slepa = new ArrayList<>();
            slepa.add(new Punkt(x, y));
            noweSlepe[x][y] = true;
            Punkt aktualny;
            int itr = 0;
            while (itr < slepa.size()) {

                aktualny = slepa.get(itr++);
                if (kierunek != null) {
                    Punkt next = Plansza.getNastepnyPunktWKierunku(aktualny, kierunek);
                    if (Plansza.jestNaKrawedzi(next)) {
                        return null;
                    }
                    if (!noweSlepe[next.x][next.y] && !zabronione[next.x][next.y]) {
                        noweSlepe[next.x][next.y] = true;
                        slepa.add(next);
                    }
                    kierunek = null;
                    continue;
                }
                Punkt[] punkty = Plansza.getSasiedniePunkty(aktualny);
                for (Punkt next : punkty) {
                    // przypisanie grupy 
                    if (Plansza.jestNaKrawedzi(next)) {
                        return null;
                    }
                    if (!zabronione[next.x][next.y] && !noweSlepe[next.x][next.y]) {
                        noweSlepe[next.x][next.y] = true;
                        slepa.add(next);
                    }
                }
            }
            for (Punkt p : slepa) {
                slepeUliczki[p.x][p.y] = true;
            }
            return slepa;
        }

        return null;
    }

    private static boolean czyGrupaOdizolowana(int nrGrupy, int[][] grupy) {
        for (int x = 1; x < Plansza.ROZMIAR_PLANSZY_X - 1; ++x) {
            if (grupy[x][1] == nrGrupy) {
                return false;
            }
            if (grupy[x][Plansza.ROZMIAR_PLANSZY_Y - 2] == nrGrupy) {
                return false;
            }
        }
        for (int y = 1; y < Plansza.ROZMIAR_PLANSZY_Y - 1; ++y) {
            if (grupy[1][y] == nrGrupy) {
                return false;
            }
            if (grupy[Plansza.ROZMIAR_PLANSZY_X - 2][y] == nrGrupy) {
                return false;
            }
        }
        return true;
    }

    private void analizujPlansze() {

    	boolean[][] polaZakazane = mapa.getPolaZakazane();
        // wyznacz grupy na planszy
        grupyPol = wyznaczGrupy(polaZakazane, null);

        // wyznacz slepe uliczki na planszy
        polaSlepeUliczki = wyznaczSlepeUliczki(polaZakazane);
    }

    private ArrayList<Przystanek> znajdzDrogeDoNieodwiedzonegoPola(Punkt pozycjaStartowa, Kierunek ostatniKierunek, boolean[][] zakazane, int poziomRekurencji) {

        boolean[][] przeanalizowane = new boolean[Plansza.ROZMIAR_PLANSZY_X][Plansza.ROZMIAR_PLANSZY_Y];
        // tablica zakazanych pol do juz przeanalizowanych
        for (int x = 0; x < Plansza.ROZMIAR_PLANSZY_X; ++x) {
            System.arraycopy(zakazane[x], 0, przeanalizowane[x], 0, Plansza.ROZMIAR_PLANSZY_Y);
        }

        ArrayList<ArrayList<Przystanek>> drogi = new ArrayList<>();
        ArrayList<Przystanek> empty = new ArrayList<>();
        empty.add(new Przystanek(pozycjaStartowa, ostatniKierunek));
        drogi.add(empty);

        ArrayList<ArrayList<Przystanek>> znalezioneDrogi = new ArrayList<>();

        ArrayList<Przystanek> najlepszaSciezka = null;
        ocenaNajlepszejSciezki = 0;

        // nie ma juz gdzie isc, jesli jestesmy na dachu, to trzeba z niego zjechac
        for (int drogiIndex = 0; drogiIndex < drogi.size(); ++drogiIndex) {
            szukajNiezakazanegoPola(drogi, drogi.get(drogiIndex), przeanalizowane, znalezioneDrogi, false);
        }

        List<Integer> najblizszeGrupy = new ArrayList<>();
        int minOdleglosc = 1000;

        for (ArrayList<Przystanek> droga : znalezioneDrogi) {
            int dlugosc = droga.size();
            if (dlugosc < minOdleglosc) {
                najblizszeGrupy.clear();
                minOdleglosc = dlugosc;
            }
            if (dlugosc == minOdleglosc) {
                Punkt koniec = droga.get(droga.size() - 1).miejsce;
                najblizszeGrupy.add(grupyPol[koniec.x][koniec.y]);
            }
        }


        for (ArrayList<Przystanek> sciezka : znalezioneDrogi) {
            List<ArrayList<Przystanek>> znalezioneSciezki = new ArrayList<>();
            List<boolean[][]> zakazaneDlaSciezki = new ArrayList<>();

            boolean[][] zakazanaDroga = new boolean[Plansza.ROZMIAR_PLANSZY_X][Plansza.ROZMIAR_PLANSZY_Y];
            // tablica zakazanych pol do juz przeanalizowanych
            for (int x = 0; x < Plansza.ROZMIAR_PLANSZY_X; ++x) {
                System.arraycopy(zakazane[x], 0, zakazanaDroga[x], 0, Plansza.ROZMIAR_PLANSZY_Y);
            }

            Punkt koniec = sciezka.get(sciezka.size() - 1).miejsce;
            zakazInnychGrup(grupyPol[koniec.x][koniec.y], zakazanaDroga, grupyPol);

            szukajZakonczenSciezki(sciezka, zakazanaDroga, znalezioneSciezki, zakazaneDlaSciezki);
            Iterator<boolean[][]> itr = zakazaneDlaSciezki.iterator();
            for (ArrayList<Przystanek> nowaSciezka : znalezioneSciezki) {
                int nowaOcena = ocenSciezke(nowaSciezka, itr.next(), najblizszeGrupy.contains(grupyPol[koniec.x][koniec.y]));

                if (ocenaNajlepszejSciezki < nowaOcena) {
                    najlepszaSciezka = nowaSciezka;
                    ocenaNajlepszejSciezki = nowaOcena;
                }
            }
        }

        if (ocenaNajlepszejSciezki == 0) {
            if (Plansza.jestNaBrzeguDachu(pozycjaDocelowa)) {
                empty.add(getSasiedniaKrawedz(pozycjaDocelowa));
                return empty;
            }
            return null;
        }

        return najlepszaSciezka;
    }

    private static void szukajNiezakazanegoPola(ArrayList<ArrayList<Przystanek>> drogi, ArrayList<Przystanek> droga, boolean[][] niedostepne, ArrayList<ArrayList<Przystanek>> znalezioneDrogi, boolean szukajKrawedzi) {
        Punkt punkt = droga.get(droga.size() - 1).miejsce;
        niedostepne[punkt.x][punkt.y] = true;
        
        Przystanek[] przystanki =  getSasiedniePrzystanki(punkt);
        for (Przystanek przystanek : przystanki)
        {
            if (przystanek != null && !niedostepne[przystanek.miejsce.x][przystanek.miejsce.y]) {
                ArrayList<Przystanek> newArrayList = new ArrayList<>(droga);
                newArrayList.add(przystanek);
                if (!szukajKrawedzi && Plansza.jestNaKrawedzi(przystanek.miejsce)) {
                    drogi.add(newArrayList);
                } else {
                    znalezioneDrogi.add(newArrayList);
                }
            }
        }
    }

    // znajduje wszystkie mozliwosci zakonczenia obecnie analizowanej sciezki
    private static void szukajZakonczenSciezki(ArrayList<Przystanek> droga, boolean[][] zakazane, List<ArrayList<Przystanek>> znalezioneSciezki, List<boolean[][]> zakazaneDlaSciezek) {
        boolean[][] zakazanaDroga = new boolean[Plansza.ROZMIAR_PLANSZY_X][Plansza.ROZMIAR_PLANSZY_Y];
        // tablica zakazanych pol do juz przeanalizowanych
        for (int x = 0; x < Plansza.ROZMIAR_PLANSZY_X; ++x) {
            System.arraycopy(zakazane[x], 0, zakazanaDroga[x], 0, Plansza.ROZMIAR_PLANSZY_Y);
        }

        Przystanek koncowy = droga.get(droga.size() - 1);
        Punkt docelowy = koncowy.miejsce;
        Kierunek kierunek = koncowy.ostatniKierunek;
        do {
            // wyjechalismy na krawedz - koniec scieżki
            if (Plansza.jestNaKrawedzi(docelowy)) {
                break;
            }
            zakazanaDroga[docelowy.x][docelowy.y] = true;

            Punkt nowyDocelowy = Plansza.getNastepnyPunktWKierunku(docelowy, kierunek);

            if (zakazanaDroga[nowyDocelowy.x][nowyDocelowy.y]) {
                // "jestesmy" przed polem zakazanym, trzeba znalezc dalszą drogę
                ArrayList<ArrayList<Przystanek>> drogi = new ArrayList<>();
                ArrayList<ArrayList<Przystanek>> znalezioneDrogi = new ArrayList<>();
                drogi.add(droga);
                szukajNiezakazanegoPola(drogi, droga, zakazanaDroga, znalezioneDrogi, true);
                // nie ma juz gdzie jechac - slepa uliczka
                if (znalezioneDrogi.isEmpty()) {
                    droga.add(null);
                    break;
                } else {
                    int znalezioneDrogiSize = znalezioneDrogi.size();
                    for (int znalezioneDrogiIndex = 0; znalezioneDrogiIndex < znalezioneDrogiSize; ++znalezioneDrogiIndex) {
                        ArrayList<Przystanek> d = znalezioneDrogi.get(znalezioneDrogiIndex);
                        szukajZakonczenSciezki(d, zakazanaDroga, znalezioneSciezki, zakazaneDlaSciezek);
                    }
                    return;
                }
            }

            droga.add(new Przystanek(nowyDocelowy, kierunek));

            docelowy = nowyDocelowy;
        } while (true);
        znalezioneSciezki.add(droga);
        if (zakazaneDlaSciezek != null) {
            zakazaneDlaSciezek.add(zakazanaDroga);
        }
    }
    
    private static final int BONUS_SCIEZEK_Z_GRUPY    = 1000000000;
    private static final int BONUS_SCIEZEK_Z_SASIADAMI = 100000000;

    private int ocenSciezke(ArrayList<Przystanek> sciezka, boolean[][] zakazane, boolean najblizszaGrupa) {

        int liczbaPolOdkrywanych = 0;
        int liczbaPolDojazdu = 0;
        boolean jestSlepaUliczka = false;
        boolean ominietyPierwszy = false;

        int liczbaNieznanychPol = 0;
        int maxPoprzednioZakazanychSasiadow = 0;

        for (Przystanek p : sciezka) {
            if (!ominietyPierwszy) {
                ominietyPierwszy = true;
                continue;
            }
            if (p == null) {
                jestSlepaUliczka = true;
                break;
            }
            if (!Plansza.jestNaKrawedzi(p.miejsce)) {
                ++liczbaPolOdkrywanych;

                if (!mapa.czyPoleZnane(p.miejsce) && mapa.getLiczbaKominow() < 12) {
                    ++liczbaNieznanychPol;
                }

                int poprzedniSasiedzi = mapa.getLiczbaZakazanychSasiednichPol(p.miejsce);
                if (maxPoprzednioZakazanychSasiadow < poprzedniSasiedzi) {
                    maxPoprzednioZakazanychSasiadow = poprzedniSasiedzi;
                }

            } else {
                ++liczbaPolDojazdu;
            }
        }

        // bezwzględnie preferuj najblizsza grupe
        int bonusNieblokujacychSciezek = najblizszaGrupa ? BONUS_SCIEZEK_Z_GRUPY : 100;
        // preferuj sciezki sasiadujace z conajmniej dwoma zakazanymi polami - sprawia ze rzadziej wystepuje problem odkladania trudnych sciezek na koniec - gdy sa juz nie do rozwiazania
        if (maxPoprzednioZakazanychSasiadow > 1) {
            bonusNieblokujacychSciezek += BONUS_SCIEZEK_Z_SASIADAMI;
        }
        // jesli nie ma szans na poprawienie obecnego wyniku - zwroc zero
        else if (ocenaNajlepszejSciezki >= BONUS_SCIEZEK_Z_SASIADAMI && ocenaNajlepszejSciezki > bonusNieblokujacychSciezek)
            return 0;

        if (jestSlepaUliczka) {
            if (czyOplacaSieZablokowac(liczbaPolOdkrywanych, 0)) {
                return liczbaPolOdkrywanych;
            }
            return 0; // nieoplacalna sciezka - lepiej dostac bonus za ukonczenie poprawne
        } else {
            
            boolean[][] slepeUliczki = new boolean[Plansza.ROZMIAR_PLANSZY_X][Plansza.ROZMIAR_PLANSZY_Y];
            // tablica zakazanych pol do juz przeanalizowanych
            for (int x = 0; x < Plansza.ROZMIAR_PLANSZY_X; ++x) {
                System.arraycopy(polaSlepeUliczki[x], 0, slepeUliczki[x], 0, Plansza.ROZMIAR_PLANSZY_Y);
            }
            Punkt temp = new Punkt(0, 0);
            int liczbaPolBlokowanych = getLiczbaBlokowanychPol(zakazane, slepeUliczki, temp);
            
            int blokowaneSlepeUliczki = temp.x;
            if (liczbaPolBlokowanych > 0 && czyOplacaSieZablokowac(liczbaPolBlokowanych, liczbaPolOdkrywanych)) {
                return liczbaPolOdkrywanych; // obnizamy ocene po to, zeby ew sciezka slepej uliczki mogla zostac wybrana
            } else {
                int liczbaPolSlepejUliczki = getLiczbaPolTworzonejSlepejUliczki(zakazane, slepeUliczki);
                if (liczbaPolBlokowanych > 0 || liczbaPolSlepejUliczki > 0) {
                    return (50 + liczbaPolOdkrywanych - liczbaNieznanychPol * 2 - (liczbaPolBlokowanych - blokowaneSlepeUliczki) * 3 - liczbaPolSlepejUliczki * 2) * 100 + (50 - liczbaPolDojazdu) + bonusNieblokujacychSciezek;
                } else {
                    if (liczbaPolOdkrywanych == 0) {
                        return liczbaPolDojazdu + 1;
                    }

                    int punkty = (50 - liczbaPolDojazdu) * 10000 + liczbaPolOdkrywanych * 100 + bonusNieblokujacychSciezek;
                    return punkty;
                }
            }
        }
    }

    private static Przystanek getSasiedniaKrawedz(Punkt punkt) {
        Przystanek[] punkty = getSasiedniePrzystanki(punkt);
        for (Przystanek p : punkty) {
            if (p != null && Plansza.jestNaKrawedzi(p.miejsce)) {
                return p;
            }
        }
        return null;
    }

    private static int getPolaSlepejUliczki(Punkt punkt, boolean[][] zakazane, boolean[][] slepeUliczki) {

        Punkt[] sasiedzi = Plansza.getSasiedniePunkty(punkt);
        Punkt dolny = sasiedzi[Kierunek.KIERUNEK_DOL.toInt()];
        Punkt gorny = sasiedzi[Kierunek.KIERUNEK_GORA.toInt()];
        Punkt lewy = sasiedzi[Kierunek.KIERUNEK_LEWO.toInt()];
        Punkt prawy = sasiedzi[Kierunek.KIERUNEK_PRAWO.toInt()];

        boolean lewyZakazany = lewy != null && zakazane[lewy.x][lewy.y];
        boolean prawyZakazany = prawy != null && zakazane[prawy.x][prawy.y];
        boolean gornyZakazany = gorny != null && zakazane[gorny.x][gorny.y];
        boolean dolnyZakazany = dolny != null && zakazane[dolny.x][dolny.y];

        int liczbaPolSlepejUliczki = 0;

        // sprawdzenie slepych uliczek na planszy
        // jesli z dwoch stron punkt jest obstawiony, to znaczy, ze moze byc wejsciem do slepej uliczki
        // lewo-prawo:
        if (lewyZakazany && prawyZakazany /*|| lewyGornyZakazany && prawyDolnyZakazany || lewyDolnyZakazany && prawyGornyZakazany  || gornyZakazany && (prawyDolnyZakazany || lewyDolnyZakazany)|| dolnyZakazany && (prawyGornyZakazany || lewyGornyZakazany) */) {

            if (!dolnyZakazany) {
                List<Punkt> slepa;
                slepa = wyznaczSlepaUliczke(punkt.x, punkt.y, Kierunek.KIERUNEK_GORA, slepeUliczki, zakazane);
                if (slepa != null) {
                    liczbaPolSlepejUliczki += slepa.size();
                }
            }
            if (!gornyZakazany) {
                List<Punkt> slepa;
                slepa = wyznaczSlepaUliczke(punkt.x, punkt.y, Kierunek.KIERUNEK_DOL, slepeUliczki, zakazane);
                if (slepa != null) {
                    liczbaPolSlepejUliczki += slepa.size();
                }
            }
        }
        if (gornyZakazany && dolnyZakazany /*|| lewyGornyZakazany && prawyDolnyZakazany || lewyDolnyZakazany && prawyGornyZakazany  || lewyZakazany && (prawyDolnyZakazany || prawyGornyZakazany)|| prawyZakazany && (lewyDolnyZakazany || lewyGornyZakazany)*/) {
            if (!lewyZakazany) {
                List<Punkt> slepa;
                slepa = wyznaczSlepaUliczke(punkt.x, punkt.y, Kierunek.KIERUNEK_PRAWO, slepeUliczki, zakazane);
                if (slepa != null) {
                    liczbaPolSlepejUliczki += slepa.size();
                }
            }
            if (!prawyZakazany) {
                List<Punkt> slepa;
                slepa = wyznaczSlepaUliczke(punkt.x, punkt.y, Kierunek.KIERUNEK_LEWO, slepeUliczki, zakazane);
                if (slepa != null) {
                    liczbaPolSlepejUliczki += slepa.size();
                }
            }
        }

        return liczbaPolSlepejUliczki;
    }

    // modyfikuje slepe uliczki
    private static int getLiczbaPolTworzonejSlepejUliczki(boolean[][] zakazane, boolean[][] slepeUliczki) {
        int rozmiarSlepejUliczki = 0;

        for (int x = 1; x < Plansza.ROZMIAR_PLANSZY_X - 1; ++x) {
            for (int y = 1; y < Plansza.ROZMIAR_PLANSZY_Y - 1; ++y) {
                if (!zakazane[x][y] && !slepeUliczki[x][y]) {
                    rozmiarSlepejUliczki += getPolaSlepejUliczki(new Punkt(x, y), zakazane, slepeUliczki);
                }
            }
        }
        return rozmiarSlepejUliczki;
    }

    private static int getLiczbaBlokowanychPol(boolean[][] zakazane, boolean[][] slepeUliczki, Punkt blokowaneSlepeUliczki) {
        int liczbaOdosabnianychPol = 0;
        List<List<Punkt>> punktyGrup = new ArrayList<>();
        int[][] grupy = wyznaczGrupy(zakazane, punktyGrup);

        int nrGrupy = 1;
        for (List<Punkt> grupa : punktyGrup) {
            if (czyGrupaOdizolowana(nrGrupy, grupy)) {

                liczbaOdosabnianychPol += grupa.size();
                for (Punkt p : grupa) {
                    if (slepeUliczki[p.x][p.y] == false) {
                        slepeUliczki[p.x][p.y] = true;
                    } else {
                        blokowaneSlepeUliczki.x++;
                    }
                }
            }
            ++nrGrupy;
        }
        return liczbaOdosabnianychPol;
    }

    private static class Przystanek {

        public Przystanek(Punkt miejsce, Kierunek kierunek) {
            this.miejsce = miejsce;
            this.ostatniKierunek = kierunek;
        }
        public Punkt miejsce;
        public Kierunek ostatniKierunek;
    }
    
    // KIEROWCA
    private Punkt pozycjaAktualna;
    private Polecenie aktualnePolecenie;

	@Override
	public void nastepnyRuch() {
		pozycjaAktualna = new Punkt(pozycjaDocelowa.x, pozycjaDocelowa.y);
		aktualnePolecenie = go();
	}

	@Override
	public Polecenie getPolecenie() {
		return aktualnePolecenie;
	}

	@Override
	public Punkt getAktualnePolozenie() {
		return pozycjaAktualna;
	}

	@Override
	public Punkt getDocelowePolozenie() {
		return pozycjaDocelowa;
	}

	@Override
	public Kierunek getAbsolutnyKierunekRuchu() {
		return getKierunekJazdy();
	}
}
