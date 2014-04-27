package com.calki.krak;

import java.util.List;

public class TryMyAstarPathFinder {

	public static void main(String[] args) {
		FieldType[][] mapVals = {
				{
					FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY
				},
				{
					FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY
				},
				{
					FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY
				},
				{
					FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY
				},
				{
					FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY
				}
		};
		
		Map map = new Map(mapVals);
		map.print();
		
		PathFinder p = new MyAstarPathFinder(Position.get(4, 4), GlobalDirection.NORTH, Position.get(0,0), map);
		List<Position> path = p.getPath();
		PathUtil.printPath(path);
		

	}

}
