package com.calki.krak;

// x - pierwsza wspolrzedna, nr wiersza macierzy
// y - druga wspolrzedna - nr kolumny
public class Map {
	public static final int SIZE = 5;
	public Map(FieldType[][] map)
	{
		this.map = map;
	}
	FieldType[][] map;
	public void markTower(Position pos)
	{
		System.out.println(pos);
		map[pos.x][pos.y] = FieldType.TOWER;
	}
	public void markEmpty(Position pos)
	{
		map[pos.x][pos.y] = FieldType.EMPTY;
	}
	public FieldType getField(Position pos)
	{
		return map[pos.x][pos.y];
	}
	public void print()
	{
		for(int i = 0; i < SIZE; ++i)
		{
			System.out.print(i);
			System.out.print("x:");
			for(int j = 0; j < SIZE; ++j)
			{
				System.out.print(map[i][j].toChar());
			}
			System.out.println();
		}
	}
}
