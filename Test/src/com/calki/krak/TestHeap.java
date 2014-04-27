package com.calki.krak;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class TestHeap {

	@Test
	public void test() {
		ArrayList<Integer> heapArray = new ArrayList<Integer>();
		Heap<Integer> heap = new Heap<Integer>(heapArray, new WeakComparatorAdapter());
		heap.insert(5);
		heap.insert(10);
		heap.insert(2);
		assertTrue(heap.removeMin() == 2);
		assertTrue(heap.removeMin() == 5);
		assertTrue(heap.removeMin() == 10);
		assertTrue(heap.size() == 0);
	}

}
