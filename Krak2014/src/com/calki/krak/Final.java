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
		
		while(true)
		{
			p = new MyAstarPathFinder(aktualnaPozycjaRobotaNaMapie, aktualnaRotacjaRobotaNaMapie, poleFinalowe, map);
			path = p.getPath();
			pose=opp.getPose();
			
			//KONIEC SETUP
			int i=0;
			
			petla_sciezki: while(i <= path.size()){
				Position docelowaPozycjaNaMapie = path.get(i);
				// wykryto wieze na polu z przodu
				if(ultraSensor.getDistance()>15 && ultraSensor.getDistance()<45){	//1 pole 
					switch(aktualnaRotacjaRobotaNaMapie){
					case NORTH:
						mapVals[(int)pose.getX()/32+1][(int)pose.getY()/32]=FieldType.TOWER;
						break;
					case SOUTH:
						mapVals[(int)pose.getX()/32-1][(int)pose.getY()/32]=FieldType.TOWER;
						break;
					case WEST:
						mapVals[(int)pose.getX()/32][(int)pose.getY()/32-1]=FieldType.TOWER;
						break;
					case EAST:
						mapVals[(int)pose.getX()/32][(int)pose.getY()/32+1]=FieldType.TOWER;
						break;
					}
					break petla_sciezki; // - wymysl nowa sciezke
				}
				
				// wykryto wieze troche dalej z przodu
				if(ultraSensor.getDistance()>48 && ultraSensor.getDistance()<77){	//2 pola
					switch(aktualnaRotacjaRobotaNaMapie){
					case NORTH:
						mapVals[(int)pose.getX()/32+2][(int)pose.getY()/32]=FieldType.TOWER;
						break;
					case SOUTH:
						mapVals[(int)pose.getX()/32-2][(int)pose.getY()/32]=FieldType.TOWER;
						break;
					case WEST:
						mapVals[(int)pose.getX()/32][(int)pose.getY()/32-2]=FieldType.TOWER;
						break;
					case EAST:
						mapVals[(int)pose.getX()/32][(int)pose.getY()/32+2]=FieldType.TOWER;
						break;
					}
					break petla_sciezki; // - wymysl nowa sciezke
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
		//ultraSensor = new UltrasonicSensor(SensorPort.S1);
	    Final f = new Final();
	    f.run();
	}
}
