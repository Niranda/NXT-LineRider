import lejos.nxt.LCD;

/*
 * Author:
 * 	Arne 'Niranda' Wandt
 * 	http://niranda.net
 * 
 * Project
 * 	NiraBot - NXT LineRider
 * 	(Mobile Agenten [HS-Wismar])
 * 
 * Date
 * 	Early 2014
 * 
 * License
 * 	GNU GPL v3 excluding a commercial/profit-oriented use
 */

/*
 * Description
 * 	
 */
public class dataExchange {
	private static int engineSpeed = 0;
	
	private static int sensorSpeed = 0;
	
	private static int linePosition = 0;
	
	
	/**
	 * main method
	 */
	public static void main(String[] args)throws Exception  {
		
	}
	
	public static void dataExchange () {
		
	}
	
	
	
	
	public static void setEngineSpeed(int speed) {
		engineSpeed = speed;
	}
	
	public static int getEngineSpeed() {
		return engineSpeed;
	}
	
	
	
	
	public static void setLinePosition(int linePosi) {
		linePosition = linePosi;
	}
	
	public int getLinePosition() {
		return linePosition;
	}
}
