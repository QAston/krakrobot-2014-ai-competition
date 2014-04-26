package com.calki.krak;

public class PositionUtil {
	
    public static double angleDiff(double currAngle, double newAngle){
      	 double zm1 = Math.abs(newAngle - currAngle);
      	 double zm2 = Math.abs(newAngle-currAngle+2*Math.PI);
      	 double zm3 = Math.abs(newAngle-currAngle-2*Math.PI);
      	 return Math.min(zm1, Math.min(zm2, zm3));
       }
    /**
     * 
     * @param x
     * @param y
     * @param heading - degrees
     * @param destX
     * @param destY
     * @return rotate angle in degrees
     */
	public static double calcDirection(double x, double y, double heading, double destX, double destY){
    	double newAngle = (Math.atan2(y-destY,x-destX));
    	return Math.toDegrees(angleDiff(Math.toRadians(heading), newAngle));
    }
}
