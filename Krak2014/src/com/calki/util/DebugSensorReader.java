package com.calki.util;

public class DebugSensorReader implements SensorReader {

	protected SensorReader reader;
	
	public DebugSensorReader(SensorReader reader) {
		this.reader = reader;
	}

	@Override
	public Integer getValue() {
		int val = reader.getValue();
		System.out.println("Odczyt sensora: " + val);
		return val;
	}
}
