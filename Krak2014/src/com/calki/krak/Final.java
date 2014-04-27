package com.calki.krak;

import java.util.List;

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
		
	}
	
	void run()
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
					FieldType.UNKNOWN, FieldType.UNKNOWN, FieldType.UNKNOWN, FieldType.UNKNOWN, FieldType.UNKNOWN
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
			System.out.println("Mapa:");
			map.print();
			p = new MyAstarPathFinder(aktualnaPozycjaRobotaNaMapie, aktualnaRotacjaRobotaNaMapie, poleFinalowe, map);
			path = p.getPath();
			pose=opp.getPose();
			
			int i=0;
			
			petla_sciezki: while(i <= path.size()){
				Position docelowaPozycjaNaMapie = path.get(i);
				
				Position docelowaForward = 
						docelowaPozycjaNaMapie.getNeighbour(
								aktualnaRotacjaRobotaNaMapie.relativeFrom(RelativeDirection.FRONT));
				
				
				int aktualnyOdczytUltraSensora = odczytajUltra();
				
				System.out.println("Ultra: "+aktualnyOdczytUltraSensora);
				
				// 1 pole do przodu
				if (docelowaPozycjaNaMapie != null && aktualnyOdczytUltraSensora<45)
				{
					map.markTower(docelowaPozycjaNaMapie);
					break petla_sciezki; // - wymysl nowa sciezke
				}
				// 2 pola do przodu
				else if (docelowaForward != null &&  aktualnyOdczytUltraSensora<80)
				{
					map.markEmpty(docelowaPozycjaNaMapie);
					map.markTower(docelowaForward);
					break petla_sciezki; // - wymysl nowa sciezke
				}
				else
				{
					if (docelowaPozycjaNaMapie != null)
						map.markEmpty(docelowaPozycjaNaMapie);
					if (docelowaForward != null)
						map.markEmpty(docelowaForward);
				}
				
				// wjezdzamy na pole podawania pilki
				if(docelowaPozycjaNaMapie==Position.get(0,0) && stanRobota == StanRobota.JAZDA_PO_PILKE){
					
					//zmiana wartosci pola finalowego
					stanRobota = StanRobota.JAZDA_Z_PILKA;
					poleFinalowe = Position.get(4, 4);
					
					//czekaj i obrot o 90
					//czekaj
					//jazda
				}
				
				// zakonczenie jazdy
				if(docelowaPozycjaNaMapie==Position.get(4,4) && stanRobota == StanRobota.JAZDA_Z_PILKA){
					Sound.setVolume(100);
					Sound.twoBeeps();
					System.exit(0);
				}
				
				pilot.rotate(Eliminacje.setDirection(pose, docelowaPozycjaNaMapie.getCartesianX(), docelowaPozycjaNaMapie.getCartesianY() ));
				//Metoda do zmiany kierunku heading robota
				pose=opp.getPose();
		    	pilot.travel(Eliminacje.setDistanceToPoint(pose, docelowaPozycjaNaMapie.getCartesianX(),docelowaPozycjaNaMapie.getCartesianY()));
		    	pose=opp.getPose();
		    	Eliminacje.lcdPokazMape();
		    	
		    	aktualnaRotacjaRobotaNaMapie = aktualnaPozycjaRobotaNaMapie.getLineDirection(docelowaPozycjaNaMapie);
		    	aktualnaPozycjaRobotaNaMapie = docelowaPozycjaNaMapie;
		    	
				i++;
			}
		}
	}
	
	
	public static void main(String[] args) {
		//
	    Final f = new Final();
	    f.run();
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
