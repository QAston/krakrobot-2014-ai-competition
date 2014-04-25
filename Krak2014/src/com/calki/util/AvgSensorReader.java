package com.calki.util;

import lejos.util.Delay;

// DECORATOR
public abstract class AvgSensorReader extends Thread implements SensorReader {
	
	private int buforSensora[];
	private int cyklBuforaSensora;
	private int bufferSize;
	private int delayMs;
	private SensorReader reader;
	
	public AvgSensorReader(SensorReader reader, Integer bufferSize, Integer delayMs)
	{
		cyklBuforaSensora = 0;
		buforSensora = new int[bufferSize];
		this.bufferSize = bufferSize;
		this.delayMs = delayMs;
		this.reader = reader;
	}
	
	public void run()
	{
		  setDaemon(true);
		  int initial = reader.getValue();
		  for (int i = 0; i < bufferSize; ++i)
		  {
			  buforSensora[i] = initial;
		  }
		  while(true)
		  {
			  Thread.yield();
			  if (delayMs != 0)
				  Delay.msDelay(delayMs);
			  synchronized(this)
			  {
				  cyklBuforaSensora++;
				  cyklBuforaSensora %= bufferSize;
				  buforSensora[cyklBuforaSensora] = reader.getValue();
			  }
		  }
	}

	@Override
	public synchronized final Integer getValue() {
		float bufAvg = 0;
		for (int i = 0; i < bufferSize; ++i)
		{
		    bufAvg += buforSensora[i];
		}
		return (int) (bufAvg / bufferSize);
	}
}
