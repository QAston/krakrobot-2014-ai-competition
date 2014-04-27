package com.calki.krak;

import java.util.List;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
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
		p = new MyAstarPathFinder(Position.get(4, 4), GlobalDirection.NORTH, Position.get(0,0), map);
		path = p.getPath();
		pose=opp.getPose();
		
		StanRobota stanRobota = StanRobota.JAZDA_PO_PILKE;
		
		//KONIEC SETUP
		int i=0;
		
		while(i <= path.size()){
			Position aktualnaPozycjaNaMapie = path.get(i);
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
				//Tutaj ustawianie pozycji
				p = new MyAstarPathFinder(Position.get(4, 4), GlobalDirection.NORTH, Position.get(0,0), map);
				path = p.getPath();
			}
			
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
			}
			
			if(aktualnaPozycjaNaMapie==Position.get(0,0)){
				
				//zmiana wartosci pola finalowego
				stanRobota = StanRobota.JAZDA_Z_PILKA;
				poleFinalowe = Position.get(4, 4);
				
				//czekaj i obrot o 90
				//czekaj
	
				//jazda
			}
			
			pilot.rotate(Eliminacje.setDirection(pose, aktualnaPozycjaNaMapie.getCartesianX(), aktualnaPozycjaNaMapie.getCartesianY() ));
			//Metoda do zmiany kierunku heading robota
			pose=opp.getPose();
	    	pilot.travel(Eliminacje.setDistanceToPoint(pose, aktualnaPozycjaNaMapie.getCartesianX(),aktualnaPozycjaNaMapie.getCartesianY()));
	    	pose=opp.getPose();
	    	Eliminacje.lcdPokazMape();
			i++;
		}
	}
	
	
	public static void main(String[] args) {
		//ultraSensor = new UltrasonicSensor(SensorPort.S1);
	    Final f = new Final();
	    f.run();
	}
}
