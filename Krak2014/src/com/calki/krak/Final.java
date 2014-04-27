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
	//final BuiltinAstarPathFinder mapa;
	final Ruchacz ruchacz;
	
	public static Pose pose;
		
	Final()
	{
		pilot = new DifferentialPilot(8.3d,8.2d,18.5d,Motor.C,Motor.A,true);
		opp = new OdometryPoseProvider(pilot);
		
	    pilot.setTravelSpeed(15);
	    pilot.setRotateSpeed(35);
		position = new PositionProviderImpl(opp);
		//mapa = new BuiltinAstarPathFinder();
		
		ruchacz = new RuchaczImpl(pilot, opp);
	}
	
	void run()
	{
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
		GlobalDirection aktualnaRotacjaRobotaNaMapie = GlobalDirection.NORTH;
		Map map = new Map(mapVals);
		map.print();
		PathFinder p = new MyAstarPathFinder(Position.get(4, 4), GlobalDirection.NORTH, Position.get(0,0), map);
		List<Position> path = p.getPath();
		pose=opp.getPose();
		
		int i=0;
		
		while(i <= path.size()){
			Position aktualnaPozycjaNaMapie = path.get(i);
			if(ultraSensor.getDistance()>15 && ultraSensor.getDistance()<45){	//1 pole 
				
				//int direction = pose.getHeading();
				//mapVals[(int)pose.getX()/32][(int)pose.getY()/32]
			}
			
			if(ultraSensor.getDistance()>48 && ultraSensor.getDistance()<77){	//2 pola
				//mapVals[(int)pose.getX()/32][(int)pose.getY()/32]
			}
			
			pilot.rotate(Eliminacje.setDirection(pose, aktualnaPozycjaNaMapie.getCartesianX(), aktualnaPozycjaNaMapie.getCartesianY() ));
			pose=opp.getPose();
	    	pilot.travel(Eliminacje.setDistanceToPoint(pose, aktualnaPozycjaNaMapie.getCartesianX(),aktualnaPozycjaNaMapie.getCartesianY()));
	    	pose=opp.getPose();
			i++;
		}
	}
	
	
	public static void main(String[] args) {
		//ultraSensor = new UltrasonicSensor(SensorPort.S1);
	    Final f = new Final();
	    f.run();
	}
}
