package com.calki.util;

public class PidInfo {
	int setPoint;
	int msDelay;
	float deadband;
	float kp;
	float ki;
	float kd;
	float limitHigh;
	float limitLow;
	float integralLimitHigh;
	float integralLimitLow;
	public PidInfo(int setPoint, int msDelay, float deadband, float kp,
			float ki, float kd, float limitHigh, float limitLow,
			float integralLimitHigh, float integralLimitLow) {
		super();
		this.setPoint = setPoint;
		this.msDelay = msDelay;
		this.deadband = deadband;
		this.kp = kp;
		this.ki = ki;
		this.kd = kd;
		this.limitHigh = limitHigh;
		this.limitLow = limitLow;
		this.integralLimitHigh = integralLimitHigh;
		this.integralLimitLow = integralLimitLow;
	}
}
