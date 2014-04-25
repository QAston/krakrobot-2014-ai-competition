package com.calki.dekarzai;

import com.calki.util.SensorReader;

import lejos.nxt.Motor;

public class Dzialo {
	
	public Dzialo(SensorReader colorSensorReader, SensorReader ultraSensorReader)
	{
		obecnyKierunekSensora = KierunekSensora.SENSOR_LEWO;
		this.colorSensorReader = colorSensorReader;
		this.ultraSensorReader = ultraSensorReader;
	}
	
	protected SensorReader colorSensorReader;
	protected SensorReader ultraSensorReader;
	protected KierunekSensora obecnyKierunekSensora;

	private static final int CZARNY = 400;
	
	public KierunekSensora getKierunekSensora()
	{
		return obecnyKierunekSensora;
	}
	
	public Kierunek getAbsulutnyKierunekSensora(Kierunek kierunekRobota)
	{
		return konwertujKierunekSensora(kierunekRobota);
	}
	
	public boolean czyWidziCzarny()
	{
		return colorSensorReader.getValue() < CZARNY;
	}
	
	public int getDist()
	{
		return ultraSensorReader.getValue();
	}
	
	protected Kierunek konwertujKierunekSensora(Kierunek kierunekRobota) {
		Kierunek kierunekSensoraPoKonwersji = null;

		switch (kierunekRobota) {
		case KIERUNEK_GORA:
			switch (obecnyKierunekSensora) {
			case SENSOR_PROSTO:
				kierunekSensoraPoKonwersji = Kierunek.KIERUNEK_GORA;
				break;
			case SENSOR_LEWO:
				kierunekSensoraPoKonwersji = Kierunek.KIERUNEK_LEWO;
				break;
			case SENSOR_PRAWO:
				kierunekSensoraPoKonwersji = Kierunek.KIERUNEK_PRAWO;
				break;
			}
			break;
		case KIERUNEK_DOL:
			switch (obecnyKierunekSensora) {
			case SENSOR_PROSTO:
				kierunekSensoraPoKonwersji = Kierunek.KIERUNEK_DOL;
				break;
			case SENSOR_LEWO:
				kierunekSensoraPoKonwersji = Kierunek.KIERUNEK_PRAWO;
				break;
			case SENSOR_PRAWO:
				kierunekSensoraPoKonwersji = Kierunek.KIERUNEK_LEWO;
				break;
			}
			break;
		case KIERUNEK_LEWO:
			switch (obecnyKierunekSensora) {
			case SENSOR_PROSTO:
				kierunekSensoraPoKonwersji = Kierunek.KIERUNEK_LEWO;
				break;
			case SENSOR_LEWO:
				kierunekSensoraPoKonwersji = Kierunek.KIERUNEK_DOL;
				break;
			case SENSOR_PRAWO:
				kierunekSensoraPoKonwersji = Kierunek.KIERUNEK_GORA;
				break;
			}
			break;
		case KIERUNEK_PRAWO:
			switch (obecnyKierunekSensora) {
			case SENSOR_PROSTO:
				kierunekSensoraPoKonwersji = Kierunek.KIERUNEK_PRAWO;
				break;
			case SENSOR_LEWO:
				kierunekSensoraPoKonwersji = Kierunek.KIERUNEK_GORA;
				break;
			case SENSOR_PRAWO:
				kierunekSensoraPoKonwersji = Kierunek.KIERUNEK_DOL;
				break;
			}
			break;
		}
		return kierunekSensoraPoKonwersji;
	}
	
	public void ustaw(KierunekSensora kierunekSenora) {
		if (kierunekSenora == obecnyKierunekSensora)
			return;

		switch (kierunekSenora) {
		case SENSOR_LEWO:
			switch (obecnyKierunekSensora) {
			case SENSOR_PROSTO:
				Motor.C.rotate(90);
				break;
			case SENSOR_PRAWO:
				Motor.C.rotate(190);
				break;
			default:
				break;
			}
			break;
		case SENSOR_PROSTO:
			switch (obecnyKierunekSensora) {
			case SENSOR_LEWO:
				Motor.C.rotate(-90);
				break;
			case SENSOR_PRAWO:
				Motor.C.rotate(100);
				break;
			default:
				break;
			}
			break;
		case SENSOR_PRAWO:
			switch (obecnyKierunekSensora) {
			case SENSOR_LEWO:
				Motor.C.rotate(-190);
				break;
			case SENSOR_PROSTO:
				Motor.C.rotate(-100);
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
		obecnyKierunekSensora = kierunekSenora;
	}
}
