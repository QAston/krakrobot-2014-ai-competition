package com.calki.krak;

public class EliminacjeWieza {
	double pozycjaX;
	double pozycjaY;
	int kolor;
	
	public EliminacjeWieza(double pozycjaX, double pozycjaY, int kolor) {
		super();
		this.pozycjaX = pozycjaX;
		this.pozycjaY = pozycjaY;
		this.kolor = kolor;
	}
	
	public EliminacjeWieza(float x, float y, int kolor) {
		this.pozycjaX=(double)x;
		this.pozycjaY=(double)y;
		this.kolor=kolor;
	}

	public void setPozycja(double pozX,double pozY){
		this.pozycjaX=pozX;
		this.pozycjaY=pozY;
	}
	public double getPozycjaX() {
		return pozycjaX;
	}
	public void setPozycjaX(double pozycjaX) {
		this.pozycjaX = pozycjaX;
	}
	public double getPozycjaY() {
		return pozycjaY;
	}
	public void setPozycjaY(double pozycjaY) {
		this.pozycjaY = pozycjaY;
	}
	public int getKolor() {
		return kolor;
	}
	public void setKolor(int kolor) {
		this.kolor = kolor;
	}
	
	
}
