package com.calki.krak;

public class WeakComparatorAdapter<TYPE extends Comparable> implements WeakComparator<TYPE> {
	@Override
	public boolean lessThan(TYPE t1, TYPE t2) {
		// TODO Auto-generated method stub
		return t1.compareTo(t2) < 0;
	}	
}
