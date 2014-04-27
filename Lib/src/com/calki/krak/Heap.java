package com.calki.krak;

import java.util.ArrayList;

public class Heap<TYPE> {
	private ArrayList<TYPE> list;
	private WeakComparator<TYPE> cmp;

	public Heap(ArrayList<TYPE> list, WeakComparator<TYPE> cmp) {
		this.list = list;
		this.cmp = cmp;
	}

	int minimum(TYPE a, int indexa, TYPE b, int indexb) {
		if (cmp.lessThan(a,b))
			return indexa;
		else
			return indexb;
	}

	public void percolateDown(int index) {

		while (true) {
			if ((2 * index + 1) <= list.size()) {

				int min = minimum(list.get(2 * index - 1), 2 * index - 1,
						list.get(2 * index), 2 * index) + 1;

				if (cmp.lessThan(list.get(min - 1), list.get(index - 1))) {
					swap(index - 1, min - 1);

					index = min;
					continue;
				}
			}

			else if (list.size() == 2 * index) {
				if (cmp.lessThan(list.get(2 * index - 1), list.get(index - 1)))
					swap(index - 1, 2 * index - 1);
			}
			break;
		}
	}

	public void make() {

		int i;

		for (i = list.size() / 2; i > 0; i--)
			percolateDown(i);
	}

	public void percolateUp(int index) {

		while (index > 1) {

			if (cmp.lessThan(list.get(index - 1), list.get(index / 2 - 1))) {

				swap(index - 1, index / 2 - 1);
			}
			index = index / 2;
		}
	}

	public void insert(TYPE value) {

		list.add(value);
		percolateUp(list.size());
	}

	protected void swap(int index1, int index2) {
		TYPE val = list.get(index1);
		list.set(index1, list.get(index2));
		list.set(index2, val);
	}

	public TYPE removeMin() {

		if (list.size() > 0) {

			TYPE retval = list.get(0);

			list.set(0, list.get(list.size() - 1));

			list.remove(list.size() - 1);

			percolateDown(1);
			return retval;
		}
		throw new RuntimeException("Empty heap!");
	}
	
	public int size()
	{
		return list.size();
	}
}
