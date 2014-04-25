package com.calki.util;

import lejos.nxt.SensorPort;
import lejos.nxt.ColorSensor;

public class ColorSensorReader implements SensorReader {
	
	private ColorSensor sensorColor;
	
	public ColorSensorReader(SensorPort port)
	{
		sensorColor = new ColorSensor(port);
		sensorColor.setFloodlight(true);
	}
	@Override
	public Integer getValue() {
		return sensorColor.getRawLightValue();
	}

}
