package com.calki.krak;

public enum FieldType {
	UNKNOWN,
	EMPTY,
	TOWER;
	
	public boolean canTravel()
	{
		return this != FieldType.TOWER;
	}
	
	public char toChar()
	{
		switch(this)
		{
		case UNKNOWN:
			return '?';
		case EMPTY:
			return ' ';
		case TOWER:
			return 'x';
		default:
			throw new RuntimeException("unknown field type");
		}
	}
}
