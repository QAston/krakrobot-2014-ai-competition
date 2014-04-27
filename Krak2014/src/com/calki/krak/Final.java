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
		
	Final()
	{
		pilot = new DifferentialPilot(8.3d,8.2d,18.5d,Motor.C,Motor.A,true);
		opp = new OdometryPoseProvider(pilot);
		
	    pilot.setTravelSpeed(15);
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
		Position poleFinalowe = Position.get(0, 0);
		GlobalDirection aktualnaRotacjaRobotaNaMapie = GlobalDirection.NORTH;
		Map map = new Map(mapVals);
		map.print();
		StanRobota stanRobota = StanRobota.JAZDA_PO_PILKE;
		
		// glowna petla
		while(true)
		{
			p = new MyAstarPathFinder(aktualnaPozycjaRobotaNaMapie, aktualnaRotacjaRobotaNaMapie, poleFinalowe, map);
			path = p.getPath();
			
			int i=0;
			
			petla_sciezki: while(i < path.size()){
				System.out.println("Mapa:");
				map.print();
				Position docelowaPozycjaNaMapie = path.get(i);
				
				int aktualnyOdczytUltraSensora = odczytajUltra();
				
				System.out.println("Ultra: "+aktualnyOdczytUltraSensora);
				
				// 1 pole do przodu
				if (docelowaPozycjaNaMapie != null && aktualnyOdczytUltraSensora<20)
				{
					map.markTower(docelowaPozycjaNaMapie);
					break petla_sciezki; // - wymysl nowa sciezke
				}
				else
				{
					if (docelowaPozycjaNaMapie != null)
						map.markEmpty(docelowaPozycjaNaMapie);
				}
				
				// wjezdzamy na pole podawania pilki
				if(docelowaPozycjaNaMapie==Position.get(0,0) && stanRobota == StanRobota.JAZDA_PO_PILKE){
					
					//zmiana wartosci pola finalowego
					stanRobota = StanRobota.JAZDA_Z_PILKA;
					poleFinalowe = Position.get(4, 4);
					
					//czekaj i obrot o 90
					pilot.travel(-20);
					Thread.sleep(1000);
					aktualnaRotacjaRobotaNaMapie = GlobalDirection.EAST;
					pilot.travel(20);
					//jazda
				}
				
				// zakonczenie jazdy
				if(docelowaPozycjaNaMapie==Position.get(4,4) && stanRobota == StanRobota.JAZDA_Z_PILKA){
					Sound.setVolume(100);
					Sound.twoBeeps();
					Button.waitForAnyPress();
				}
				
				RelativeDirection turn = aktualnaRotacjaRobotaNaMapie.turnAsRelative(aktualnaPozycjaRobotaNaMapie.getLineDirection(docelowaPozycjaNaMapie));
				
				move(turn);
				/*
				pilot.rotate(PositionUtil.calcDirection(pose.getX(), pose.getY(), pose.getHeading(), docelowaPozycjaNaMapie.getCartesianX(),  docelowaPozycjaNaMapie.getCartesianY()));
				//Metoda do zmiany kierunku heading robota
				pose=opp.getPose();
		    	pilot.travel(PositionUtil.calcDistance(pose.getX(), pose.getY(), pose.getHeading(), docelowaPozycjaNaMapie.getCartesianX(),docelowaPozycjaNaMapie.getCartesianY()));
		    	pose=opp.getPose();
		    	//Eliminacje.lcdPokazMape();*/
		    	
		    	aktualnaRotacjaRobotaNaMapie = aktualnaPozycjaRobotaNaMapie.getLineDirection(docelowaPozycjaNaMapie);
		    	aktualnaPozycjaRobotaNaMapie = docelowaPozycjaNaMapie;
		    	
				i++;
			}
		}
	}
	
	public void move(RelativeDirection dir)
	{
		switch(dir)
		{
			case FRONT:
				pilot.travel(32);
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
	
	
	public static void main(String[] args) {
		//
	    Final f = new Final();
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
