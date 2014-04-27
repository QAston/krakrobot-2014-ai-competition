package com.calki.krak;

import java.util.List;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Pose;

public class Final {
	public static UltrasonicSensor ultraSensor;
	
	final DifferentialPilot pilot;
	final OdometryPoseProvider opp;
	final PositionProvider position;
	
	public PathFinder p;
	public List<Position> path;
	
	public static Pose pose;
	
	public Position waitForBall = Position.get(0,0);
		
	Final()
	{
		pilot = new DifferentialPilot(4.45d, 12.18, Motor.A, Motor.C,true);
		opp = new OdometryPoseProvider(pilot);
		
		pilot.setAcceleration(35);
		pilot.setTravelSpeed(30);
		pilot.setRotateSpeed(35);
		//opp.setPose(new Pose(x, y, heading));
		position = new PositionProviderImpl(opp);
		ultraSensor = new UltrasonicSensor(SensorPort.S1);
		opp.setPose(new Pose(162, 162, 0));
	}
	
	void run() throws Throwable
	{
		//SETUP
		FieldType[][] mapVals = {
				{
					FieldType.UNKNOWN, FieldType.UNKNOWN, FieldType.UNKNOWN, FieldType.UNKNOWN, FieldType.UNKNOWN
				},
				{
					FieldType.UNKNOWN, FieldType.UNKNOWN, FieldType.UNKNOWN, FieldType.UNKNOWN, FieldType.UNKNOWN
				},
				{
					FieldType.UNKNOWN, FieldType.UNKNOWN, FieldType.UNKNOWN, FieldType.UNKNOWN, FieldType.UNKNOWN
				},
				{
					FieldType.UNKNOWN, FieldType.UNKNOWN, FieldType.UNKNOWN, FieldType.UNKNOWN, FieldType.UNKNOWN
				},
				{
					FieldType.UNKNOWN, FieldType.UNKNOWN, FieldType.UNKNOWN, FieldType.UNKNOWN, FieldType.EMPTY
				}
		};
		Position aktualnaPozycjaRobotaNaMapie = Position.get(4, 4);
		Position poleFinalowe = waitForBall;
		GlobalDirection aktualnaRotacjaRobotaNaMapie = GlobalDirection.NORTH;
		Map map = new Map(mapVals);
		map.print();
		StanRobota stanRobota = StanRobota.JAZDA_PO_PILKE;
		
		// glowna petla
		while(true)
		{
			System.out.println("Glowna");
			p = new MyAstarPathFinder(aktualnaPozycjaRobotaNaMapie, aktualnaRotacjaRobotaNaMapie, poleFinalowe, map);
			path = p.getPath();
			
			int i=0;
			
			while(i < path.size()){
				
				System.out.println("Path");
				map.print();

				Position docelowaPozycjaNaMapie = path.get(i);
				
				RelativeDirection turn = aktualnaRotacjaRobotaNaMapie.turnAsRelative(aktualnaPozycjaRobotaNaMapie.getLineDirection(docelowaPozycjaNaMapie));
				
				look(turn);
				
				aktualnaRotacjaRobotaNaMapie = aktualnaPozycjaRobotaNaMapie.getLineDirection(docelowaPozycjaNaMapie);
				
				int aktualnyOdczytUltraSensora = odczytajUltra();
				
				System.out.println("Ultra: "+aktualnyOdczytUltraSensora);
				
				// 1 pole do przodu
				if (docelowaPozycjaNaMapie != null && aktualnyOdczytUltraSensora<35)
				{
					map.markTower(docelowaPozycjaNaMapie);
					break;
				}
				else
				{
					if (docelowaPozycjaNaMapie != null)
						map.markEmpty(docelowaPozycjaNaMapie);
				}
				
				move(turn);
		    	
		    	aktualnaRotacjaRobotaNaMapie = aktualnaPozycjaRobotaNaMapie.getLineDirection(docelowaPozycjaNaMapie);
		    	aktualnaPozycjaRobotaNaMapie = docelowaPozycjaNaMapie;
		    	
				// wjezdzamy na pole podawania pilki
				if(aktualnaPozycjaRobotaNaMapie==waitForBall && stanRobota == StanRobota.JAZDA_PO_PILKE){
					
					//zmiana wartosci pola finalowego
					stanRobota = StanRobota.JAZDA_Z_PILKA;
					poleFinalowe = Position.get(4, 4);
					
					switch(aktualnaRotacjaRobotaNaMapie)
					{
					case EAST:
						break;
					case WEST:
						look(RelativeDirection.BACK);
						break;
					case NORTH:
						look(RelativeDirection.RIGHT);
						break;
					case SOUTH:
						look(RelativeDirection.LEFT);
						break;
						
					}
					pilot.travel(-30);
					Thread.sleep(5000);
					aktualnaRotacjaRobotaNaMapie = GlobalDirection.EAST;
					pilot.travel(10);
					break;
				}
				
				// zakonczenie jazdy
				if(docelowaPozycjaNaMapie==Position.get(4,4) && stanRobota == StanRobota.JAZDA_Z_PILKA){
					Sound.setVolume(100);
					Sound.twoBeeps();
					Button.waitForAnyPress();
				}
		    	
				i++;
			}
		}
	}
	
	public void move(RelativeDirection dir)
	{
		pilot.travel(32);
	}
	
	public void look(RelativeDirection dir)
	{
		switch(dir)
		{
			case FRONT:
				break;
			case BACK:
				pilot.rotate(180);
				break;
			case LEFT:
				pilot.rotate(-90);
				break;
			case RIGHT:
				pilot.rotate(90);
				break;
		}
	}
	
	
	public static void main(String[] args) throws Throwable {
		//
	    Final f = new Final();
	    //f.run();
	    try {
			f.run();
		} catch (Throwable e) {
			Sound.setVolume(100);
			Sound.twoBeeps();
			Button.waitForAnyPress();
		}
	}
	
	public int odczytajUltra()
	{
		int suma = 0;
		int liczbaProb = 5;
		int zaliczoneProby = 0;
		for(int i = 0; i < liczbaProb; ++i)
		{
			int proba = ultraSensor.getDistance();
			if (proba == 255)
				continue;
			suma += proba;
			++zaliczoneProby;
		}
		if (zaliczoneProby == 0)
			return 255;
		return suma / zaliczoneProby;
	}
}
