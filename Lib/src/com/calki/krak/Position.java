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
	
	public int getCartesianX(){
		
		return 128 - this.x*32;
	}
	
	public int getCartesianY(){
		return 128 - this.y*32;
	}
	
	public int toIndex()
	{
		return x*Map.SIZE + y;
	}
	
	public String toString()
	{
		return "Pos"+x+y;
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
	    	  return get(this.x- 1, this.y );
	       case SOUTH:
		      return get(this.x+1, this.y );
	       case EAST:
	    	   return get(this.x, this.y +1);
	       case WEST:
	    	   return get(this.x, this.y -1);
	    }
	    return null; 
	}
	
	GlobalDirection getLineDirection(Position target)
	{
		if (target.x > x)
		{
			return GlobalDirection.SOUTH;
		}
		if (target.x < x)
		{
			return GlobalDirection.NORTH;
		}
		if (target.y < y)
		{
			return GlobalDirection.WEST;
		}
		if (target.y > y)
		{
			return GlobalDirection.EAST;
		}
		throw new RuntimeException("Get line direction wrong value");
	}
}
