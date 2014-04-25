package com.calki.krak;

public class Position {
	int x;
	int y;
	
	public Position(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public int toIndex()
	{
		return x*Mapa.ROZMIAR_MAPY + y;
	}
	
	public boolean isValid()
	{
		return this.x >= 0 && this.x < Mapa.ROZMIAR_MAPY && this.y >= 0 && this.y < Mapa.ROZMIAR_MAPY;
	}
	/*
	Position getNeighbour(GlobalDirection globalDirection)
	{
	    Position ret;
	    switch(globalDirection)
	    {
	       case NORTH:
	          ret.x = this.x;
	          ret.y = this.y - 1;
	       break;
	       case SOUTH:
	          ret.x = this.x;
	          ret.y = this.y + 1;
	       break;
	       case EAST:
	          ret.x = this.x + 1;
	          ret.y = this.y;
	       break;
	       case WEST:
	          ret.x = this.x - 1;
	          ret.y = this.y;
	       break;
	    }
	}*/
}
