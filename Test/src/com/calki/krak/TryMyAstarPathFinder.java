package com.calki.krak;

import java.util.List;

import lejos.robotics.navigation.Pose;

public class TryMyAstarPathFinder {
	public static double angleDiff(double currAngle, double newAngle){
	   	 double zm1 = Math.abs(newAngle - currAngle);
	   	 double zm2 = Math.abs(newAngle-currAngle+2*Math.PI);
	   	 double zm3 = Math.abs(newAngle-currAngle-2*Math.PI);
	   	 return Math.min(zm1, Math.min(zm2, zm3));
	    }
	    //---- obie funkcje przyjmuja wartoœci dodatnie, zarówno dla x jak i y --//
	    //zwraca wartoœæ o jak¹ ma obróciæ siê robot aby staæ przodem do docelowych wspó³rzênych x,y
	    public static double setDirection(Pose actualPose, double destX, double destY){
	    	double newAngle = (Math.atan2(actualPose.getLocation().getY()-destY,actualPose.getLocation().getX()+destX));
	    	System.out.println(-(Math.toDegrees(angleDiff(Math.toRadians(actualPose.getHeading()), newAngle))));
	    	return -(Math.toDegrees(angleDiff(Math.toRadians(actualPose.getHeading()), newAngle)));
	    }//u¿ycie:
	    // pilot.rotate(setDirection(pose, 128.0d,128.0d); -> podaje mu aktualna pozycjê oraz punkt w którego strone ma sie obórcic
	    
	    public static double setDistanceToPoint(Pose actualPose, double destX, double destY){
	    	destX = -destX;
	    	double d = Math.sqrt((Math.pow((destX - actualPose.getLocation().getX()),2.0d)+(Math.pow((destY - actualPose.getLocation().getY()), 2.0d))));
	    	return d;
	    }

	public static void main(String[] args) {
		FieldType[][] mapVals = {
				{
					FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY
				},
				{
					FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY
				},
				{
					FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY, FieldType.TOWER
				},
				{
					FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY, FieldType.TOWER, FieldType.EMPTY
				},
				{
					FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY, FieldType.EMPTY
				}
		};
		
		Map map = new Map(mapVals);
		map.print();
		PathFinder p = new MyAstarPathFinder(Position.get(4, 4), GlobalDirection.NORTH, Position.get(0,0), map);
		List<Position> path = p.getPath();
		PathUtil.printPath(path);
		
int i=0;
		
		while(i < path.size()){
			Position aktualnaPozycja = path.get(i);
			//pilot.rotate(Eliminacje.setDirection(pose, aktualnaPozycja.getCartesianX(),aktualnaPozycja.getCartesianY()));
	    	System.out.println("X:"+aktualnaPozycja.getCartesianX()+" Y:"+aktualnaPozycja.getCartesianY());
	    	System.out.println("Wieza:"+aktualnaPozycja.getCartesianX()+32);
	    	mapVals[(int)aktualnaPozycja.getCartesianX()/32][(int)aktualnaPozycja.getCartesianY()/32]=FieldType.TOWER;
			//pose=opp.getPose();
	    	//pilot.travel(Eliminacje.setDistanceToPoint(pose, aktualnaPozycja.getCartesianX(),aktualnaPozycja.getCartesianY()));
	    	//pose=opp.getPose();
			i++;
		}

	}

}
