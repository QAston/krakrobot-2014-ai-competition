package com.calki.krak;

public class Position {
	
	public static Position get(int x, int y)
	{
		if (!isValid(x, y))
			return null;
		return positions[x][y];
	}
	
	private static Position[][] positions = new Position[Map.SIZE][Map.SIZE];
	
	static 
	{
		for(int i = 0; i <Map.SIZE; ++i)
		{
			for(int j = 0; j <Map.SIZE; ++j)
			{
				positions[i][j] = new Position(i, j);
			}
		}
	}
	public final int x;
	public final int y;
	
	private Position(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public int toIndex()
	{
		return x*Map.SIZE + y;
	}
	
	public static boolean isValid(int x, int y)
	{
		return x >= 0 && x < Map.SIZE && y >= 0 && y < Map.SIZE;
	}
	
	Position getNeighbour(GlobalDirection globalDirection)
	{
	    switch(globalDirection)
	    {
	       case NORTH:
	    	  return get(this.x, this.y - 1);
	       case SOUTH:
		      return get(this.x, this.y +1);
	       case EAST:
	    	   return get(this.x+1, this.y );
	       case WEST:
	    	   return get(this.x-1, this.y );
	    }
	    return null; 
	}
}
