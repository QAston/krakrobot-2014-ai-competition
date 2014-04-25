package com.calki.dekarzai;


/**
 * 
 * @author QAston <qaston@gmail.com>
 */
public enum KierunekSensora {

	SENSOR_PROSTO(1), SENSOR_LEWO(0), SENSOR_PRAWO(2);

	private int value;

	KierunekSensora(int val) {
		this.value = val;
	}

	public int toInt() {
		return this.value;
	}

	public KierunekSensora przeciwny() {
		switch (this) {
		case SENSOR_LEWO:
			return SENSOR_PRAWO;
		case SENSOR_PRAWO:
			return SENSOR_LEWO;
		case SENSOR_PROSTO:
			return null;
		default:
			return null;
		}
	}
}

