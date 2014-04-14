
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
	private static int colorWhite;																// Wert auf weißen Oberflächen
	private static int colorBlack;																// Wert auf schwarzen Oberflächen
	private static int colorTolerance;															// Toleranz in Einheiten
	
	private static int maxLeft;																	// maximale Sensorstellung: links
	private static int maxRight;																// maximale Sensorstellung: rechts
	private static int position = 0;															// aktuelle Sensorposition
	
	private static boolean 	go = true;															// TRUE = arbeitend, FALSE = nicht arbeitend
	private static int 		direction = 1;														// Sensor Bewegungsrichtung (1 = right, -1 = left)
	private static int 		lastDirection;														// Direction before change
	private static boolean 	changeDirection = false;											// true, if direction-change is incoming (while-break)
	private static boolean 	blackBeforeReverse = false;											// true, if before a reverse was black found, false if not
	private static boolean 	foundBlack = false;													// set true, if black was found (just reverse set it false)
	private static int 		reverseCounter = 0;													// reverse-counter, if there wasn't a black-found before
	
	private static int hardwareLagg = 100;														// Thread sleeping for ...ms to compensate the hardware-lagg
	private static int sensorSpeed = 300;														// speed of the sensor, calculates braking-distance
	private static int brakingCompensation;														// the faster the sensor, the higher the braking-distance - a balancer
	
	
	
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
	
	private static int defaultMaxLeft = -60;													// 1 sensor-degree == 1,5 degree
	private static int defaultMaxRight = 60;
	

	
	/* *****************************
	 * METHODS
	 * *****************************/
	
	/**
	 * main method
	 */
	public static void main(String[] args)throws Exception  {
//		while (!Button.ESCAPE.isDown()) {
//			LCD.drawString("Posi 0", 1, 1);
//			LCD.drawString("Enter!", 1, 2);
//			
//			if (Button.RIGHT.isDown()) {
//				Motor.B.rotate(-5);
//			}
//			else if (Button.LEFT.isDown()) {
//				Motor.B.rotate(5);
//			}
//			else if (Button.ENTER.isDown()) {
//				Motor.B.resetTachoCount();
//			}
//		
//			LCD.drawInt(Motor.C.getPosition(), 1, 3);
//		}
		searchLine();
		
	}
	
	
	/* *****************************
	 * OPERATIONS
	 * *****************************/
	
	/**
	 * Sensor will find and follow the black line.
	 * The variable 'position' will be set to the current position of the sensor.
	 * 
	 * At start the sensor has to be in center-position!
	 * 
	 * @throws InterruptedException 
	 */
	public static void searchLine() throws InterruptedException {
		Motor.B.resetTachoCount();
		setSpeed(sensorSpeed);
		
		setDefaultColor();
		setDefaultMovement();
		
		while(go) {
			changeDirection = false;
			
			if (direction == 1) {
				Motor.B.backward();
			}
			
			if (direction == -1) {
				Motor.B.forward();
			}

			Thread.sleep(hardwareLagg);																		// compensate hardware-lagg
			
			while (checkMovingRange() && !changeDirection) {												// sensor is in range...
				
				if (checkColor(colorWhite)) {																// Detected white surface
					
				}
				else if (checkColor(colorBlack)) {															// Detected black surface
					foundBlack = true;
				}
				else {																						// Detected sth. other...
					/* ignore */
				}
				
				if (Button.ESCAPE.isDown()) { break; }
			}
			
			reverseDirection();
			
			if (Button.ESCAPE.isDown()) { break; }
		}
	}
	
	
	/**
	 * This will reverse the moving direction of the sensor.
	 */
	private static void reverseDirection() {
		Motor.B.stop();
		direction = direction * -1;
		
		if (foundBlack) {
			blackBeforeReverse = true;
			foundBlack = false;
			reverseCounter = 0;
		}
		else {
			reverseCounter++;
		}
	}
	
	
	/**
	 * Check, if a given color is in the tolerance-range
	 * 
	 * @param paraColor color which should be checked
	 */
	private static boolean checkColor(int paraColor) {
		if (ls.readNormalizedValue() >= (paraColor - colorTolerance) &&
			ls.readNormalizedValue() <= (paraColor + colorTolerance))
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	
	/**
	 * Check, if the sensor is in the moving-range
	 */
	private static boolean checkMovingRange() {
		if (Motor.B.getTachoCount() * -1 > maxLeft + brakingCompensation &&								// sensor is in range
			Motor.B.getTachoCount() * -1 < maxRight - brakingCompensation)
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	
	/**
	 * returns the position of the black line (middle between two sensorpositions)
	 * 
	 * @param paraOne one site of the position
	 * @param paraTwo other site of the position
	 */
	private static int calcDirection(int paraOne, int paraTwo) {
		return ((paraOne + paraTwo) / 2) * -1;															// Negation, wegen Übersetzung
	}
	
	
	/* *****************************
	 * SETTER
	 * *****************************/
	
	/**
	 * Set the moving-speed of the sensor and calculate some extra space for
	 * an extra breaking-range
	 * 
	 * @param paraSpeed
	 */
	private static void setSpeed(int paraSpeed) {
		Motor.B.setSpeed(paraSpeed);
		brakingCompensation = Math.round((paraSpeed / 200) * 10);
	}
	
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
