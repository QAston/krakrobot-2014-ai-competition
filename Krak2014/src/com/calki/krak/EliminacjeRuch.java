package com.calki.krak;

public class EliminacjeRuch {
	double pozycjaX;
	double pozycjaY;
	
	public EliminacjeRuch(double pozX, double pozY){
		this.pozycjaX=pozX;
		this.pozycjaY=pozY;
	}
	
	public void setPozycjaX(double pozX){
		this.pozycjaX=pozX;
	}
	
	public void setPozycjaY(double pozY){
		this.pozycjaY=pozY;
	}
	
	public void setPozycja(double pozX,double pozY){
		this.pozycjaX=pozX;
		this.pozycjaY=pozY;
	}
	
	public double getPozycjaX(){
		return this.pozycjaX;
	}
	
	public double getPozycjaY(){
		return this.pozycjaY;
	}
}
