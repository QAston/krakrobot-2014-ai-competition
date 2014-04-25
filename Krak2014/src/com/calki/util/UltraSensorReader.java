package com.calki.util;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class UltraSensorReader implements SensorReader {

	private UltrasonicSensor sensorUltra;
	
	public UltraSensorReader(SensorPort port)
	{
		sensorUltra = new UltrasonicSensor(port);
	}

	@Override
	public Integer getValue() {
		return sensorUltra.getDistance();
	}
}
