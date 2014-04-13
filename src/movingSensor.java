
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

import lejos.nxt.*; 
import lejos.robotics.navigation.*;

/* *****************************
 * Description
 * 	Basic functions of the robot's moving sensor in the front
 *  (180 degree swiveling)
 * *****************************/

public class movingSensor {
	/* *****************************
	 * VARIABLE DECLARATION
	 * *****************************/
	private static int colorWhite;																// Wert auf weiﬂen Oberfl‰chen
	private static int colorBlack;																// Wert auf schwarzen Oberfl‰chen
	private static int colorTolerance;															// Toleranz in Einheiten
	
	private static int maxLeft;																	// maximale Sensorstellung: links
	private static int maxRight;																// maximale Sensorstellung: rechts
	private static int direction = 0;															// aktuelle Sensorposition
	
	private static boolean go = true;															// TRUE = arbeitend, FALSE = nicht arbeitend
	
	/*
	 * OBJECTS
	 */
	private static LightSensor ls = new LightSensor(SensorPort.S1);								// LightSensor initiieren
	
	/*
	 * DEFAULTS
	 */
	private static int defaultColorWhite = 400;
	private static int defaultColorBlack = 320;
	private static int defaultColorTolerance = 25;
	
	private static int defaultMaxLeft = -90;
	private static int defaultMaxRight = 90;
	

	/* *****************************
	 * METHODS
	 * *****************************/
	
	/**
	 * main method
	 */
	public static void main(String[] args)throws Exception  {
		while (!Button.ESCAPE.isDown()) {
			LCD.drawString("Posi 0", 1, 1);
			LCD.drawString("Enter!", 1, 2);
			
			if (Button.RIGHT.isDown()) {
				Motor.B.rotate(-5);
			}
			else if (Button.LEFT.isDown()) {
				Motor.B.rotate(5);
			}
			else if (Button.ENTER.isDown()) {
				Motor.B.resetTachoCount();
			}
		
			LCD.drawInt(Motor.C.getPosition(), 1, 3);
		}
		
	}
	
	
	/* *****************************
	 * OPERATIONS
	 * *****************************/
	
	
	public static void searchLine() {
		while(go) {
			
			
			while (Motor.B.isMoving()) {
				if (Motor.B.getTachoCount() * -1 <= maxLeft ||
						Motor.B.getTachoCount() * -1 >= maxRight) {
					// RICHTUNG UMKEHREN TODO
				}
				else {
					if (ls.readNormalizedValue() >= (colorWhite - colorTolerance) &&							// Detected white surface
						ls.readNormalizedValue() <= (colorWhite + colorTolerance)) {
						
					}
					else if (	ls.readNormalizedValue() >= (colorBlack - colorTolerance) &&					// Detected black surface
								ls.readNormalizedValue() <= (colorBlack + colorTolerance)) {
						
					}
					else {																						// Detected sth. other...
						/* ??? */
					}
				}
				
			}
			
		}
	}
	
	
	/**
	 * returns the position of the black line (middle between two sensorpositions)
	 * 
	 * @param paraOne one site of the position
	 * @param paraTwo other site of the position
	 */
	private static int calcDirection(int paraOne, int paraTwo) {
		return ((paraOne + paraTwo) / 2) * -1;															// Negation, wegen ‹bersetzung
	}
	
	
	/* *****************************
	 * SETTER
	 * *****************************/
	
	/**
	 * set color-reflections
	 *  all parameters are in 'normalized values' (not percentages!)
	 * 
	 * @param valueWhite reflection of white surfaces
	 * @param valueBlack reflection of black surfaces
	 * @param valueTolerance tolerance range
	 */
	public static void setColor(int valueWhite, int valueBlack, int valueTolerance) {
		colorWhite = valueWhite;
		colorBlack = valueBlack;
		colorTolerance = valueTolerance;
	}
	
	
	/**
	 * set sensor's "freedom of movement"
	 * 	x < 0	Left
	 *  x = 0	Center
	 *  x > 0	right
	 * 
	 * @param paraLeft maximum in the left (value lower then 0)
	 * @param paraRight maximum in the right (value higher then 0)
	 */
	public static void setMovement(int paraLeft, int paraRight) {
		maxLeft = paraLeft;
		maxRight = paraRight;
	}
	

	/**
	 * Set the default values of color-reflections
	 */
	public static void setDefaultColor() {
		setColor(defaultColorWhite, defaultColorBlack, defaultColorTolerance);					// White, Black, Tol.
	}
	
	
	/**
	 * Set the default moving-range of th sensor
	 */
	public static void setDefaultMovement() {
		setMovement(defaultMaxLeft, defaultMaxRight);											// left, right
	}

}
