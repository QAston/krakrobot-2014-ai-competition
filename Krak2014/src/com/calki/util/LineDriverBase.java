package com.calki.util;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;
import lejos.util.PIDController;

public abstract class LineDriverBase {

	protected PIDController pidControler;
    protected DifferentialPilot pilot;
    protected float side = -1;
    protected int lastValue;
    protected int lastPct;
    protected boolean stop = false;
    protected SensorReader sensor;
    protected PidInfo pidInfo;
    protected boolean forward = true;
    
    public LineDriverBase(DifferentialPilot pilot, PidInfo pidInfo, SensorReader sensor)
    {
    	this.pilot = pilot;
    	this.pidInfo = pidInfo;
        this.sensor = sensor;
        reset();
    }

	public void drive() {
		while (!beforeSteer())
		{
			lastValue = getValue();
			lastPct = pidControler.doPID(lastValue);
			if (onSteer())
				break;
			float rate = this.side * this.lastPct;
			if (forward)
				this.pilot.steer(rate);
			else
				this.pilot.steerBackward(rate);
		}
	}
	
	public void lineOnLeft()
	{
		side = -1;
	}
	
	public void lineOnRight()
	{
		side = 1f;
	}
	
	public void reset()
	{
    	pidControler = new PIDController(pidInfo.setPoint, pidInfo.msDelay);
    	pidControler.setPIDParam(PIDController.PID_DEADBAND, pidInfo.deadband);
    	pidControler.setPIDParam(PIDController.PID_KP, pidInfo.kp);
    	pidControler.setPIDParam(PIDController.PID_KI, pidInfo.ki);
    	pidControler.setPIDParam(PIDController.PID_KD, pidInfo.kd);
    	pidControler.setPIDParam(PIDController.PID_LIMITHIGH, pidInfo.limitHigh);
        pidControler.setPIDParam(PIDController.PID_LIMITLOW, pidInfo.limitLow);
        pidControler.setPIDParam(PIDController.PID_I_LIMITHIGH, pidInfo.integralLimitHigh);
        pidControler.setPIDParam(PIDController.PID_I_LIMITLOW, pidInfo.integralLimitLow);
        this.pilot.reset();
	}
	
	/**
	 * @return true - stops driver
	 */
	protected abstract boolean onSteer();
	/**
	 * @return true - stops driver
	 */
	protected abstract boolean beforeSteer();
	protected int getValue()
	{
		return sensor.getValue();
	}
}
