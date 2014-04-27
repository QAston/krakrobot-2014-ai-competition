package com.calki.krak;

import java.util.List;

public class PathUtil {
	public static List<RelativeDirection> pathToMoves(List<Position> path)
	{
		return null;
	}
	
	public static void printPath(List<Position> path)
	{
		for(Position p:path)
		{
			System.out.print("Path:"+p.x +','+ p.y+"=>");
		}
	}
	
	public static void printOnMap(List<Position> path, Map map)
	{
		
	}
}
