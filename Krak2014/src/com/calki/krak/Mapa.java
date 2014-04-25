package com.calki.krak;

import lejos.robotics.pathfinding.Node;

public class Mapa {
	public static final int ROZMIAR_MAPY = 5;
	Node[] nodes;
	
	Mapa()
	{
		for(int i = 0; i < ROZMIAR_MAPY; ++i)
		{
			for(int j = 0; j < ROZMIAR_MAPY; ++j)
			{
				nodes[i] = new Node(i, j);
			}
		}
		
		for(int i = 0; i < ROZMIAR_MAPY; ++i)
		{
			for(int j = 0; j < ROZMIAR_MAPY; ++j)
			{
				
			}
		}
	}
	
	public static int toIndex(int x, int y)
	{
		return x * ROZMIAR_MAPY + y;
	}
	
	public static Position toPosition(int index)
	{
		return new Position(index / ROZMIAR_MAPY, index % ROZMIAR_MAPY);
	}
}
