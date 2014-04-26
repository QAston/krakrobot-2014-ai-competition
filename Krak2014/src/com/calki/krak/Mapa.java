package com.calki.krak;

import lejos.robotics.pathfinding.Node;

public class Mapa {
	
	// uzywamy metryki taksówkowej
	class TaxiNode extends Node
	{

		public TaxiNode(float x, float y) {
			super(x, y);
		}
		
		@Override
		protected float calculateG(Node neighbor) {
			
			return Math.abs(this.x - neighbor.x) +  Math.abs(this.y - neighbor.y);
		}
		
		@Override
		protected float calculateH(Node goal) {
			return Math.abs(this.x - goal.x) +  Math.abs(this.y - goal.y);
		}
		
	}
	public static final int ROZMIAR_MAPY = 5;
	Node[] nodes;
	
	Mapa()
	{
		for(int i = 0; i < ROZMIAR_MAPY; ++i)
		{
			for(int j = 0; j < ROZMIAR_MAPY; ++j)
			{
				nodes[toIndex(i, j)] = new TaxiNode(i, j);
			}
		}
		
		for(int i = 0; i < ROZMIAR_MAPY; ++i)
		{
			for(int j = 0; j < ROZMIAR_MAPY; ++j)
			{
				Position p = new Position(i, j);
				Node n = nodes[toIndex(i, j)];
				for(GlobalDirection globalDirection : GlobalDirection.values())
				{
					Position neighbour = p.getNeighbour(globalDirection);
					if (neighbour.isValid())
					{
						n.addNeighbor(nodes[neighbour.toIndex()]);
					}
				}
			}
		}
	}
	
	public void markWall(Position pos)
	{
		Node wall = nodes[pos.toIndex()];
		
		for(Node n:wall.getNeighbors())
		{
			n.removeNeighbor(wall);
			wall.removeNeighbor(n);
		}
	}
	
	public Node getNode(int x, int y)
	{
		return nodes[toIndex(x, y)];
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
