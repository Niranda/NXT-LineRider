
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

public class movingSensor extends Thread {
	/* *****************************
	 * VARIABLE DECLARATION
	 * *****************************/
	private static int colorWhite;																// Wert auf weißen Oberflächen
	private static int colorBlack;																// Wert auf schwarzen Oberflächen
	private static int colorTolerance;															// Toleranz in Einheiten
	
	private static int maxLeft;																	// maximale Sensorstellung: links
	private static int maxRight;																// maximale Sensorstellung: rechts
	
	private static int linePosition = 0;														// line's position in normal degree
	private static int lastPosition = 0;														// last seen position
	private static int penultimatePosition = 0;													// penultimated position (last memorized)
	
	private static boolean 	go = true;															// TRUE = arbeitend, FALSE = nicht arbeitend
	private static int 		direction = 1;														// Sensor Bewegungsrichtung (1 = right, -1 = left)
	private static int 		lastDirection;														// Direction before change
	private static boolean 	changeDirection = false;											// true, if direction-change is incoming (while-break)
	private static boolean 	blackBeforeReverse = false;											// true, if before a reverse was black found, false if not
	private static boolean 	foundBlack = false;													// set true, if black was found (just reverse set it false)
	private static int 		reverseCounter = 0;													// reverse-counter, if there wasn't a black-found before
	
	private static int hardwareLagg = 100;														// Thread sleeping for ...ms to compensate the hardware-lagg
	private static int sensorSpeed = 50;														// speed of the sensor, calculates braking-distance
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
		LCD.drawString("SENSOR TESTMODE", 1, 1);
		LCD.drawString("Hit ENTER + ESC to stop", 1, 2);
		searchLine();
	}
	
	
	public void run() {
		go = true;
		searchLine();
	}
	
	public void stop() {
		go = false;
	}
	
	
	/**
	 * Sensor will find and follow the black line.
	 * The variable 'position' will be set to the current position of the sensor.
	 * 
	 * At start the sensor has to be in center-position!
	 * 
	 * @throws InterruptedException 
	 */
	public static void searchLine() {
		Motor.B.resetTachoCount();																	// 0 degree at start :-)
		
		setSpeed(sensorSpeed);																		// set some values..
		setDefaultColor();
		setDefaultMovement();
		
		while(go) {																					// start moving!
			changeDirection = false;
			
			if (direction == 1) {																	// turn right
				turnRight();
			}
			else if (direction == -1) {																// turn left
				turnLeft();
			}

			try {																					// workaround for multithreading
				Thread.sleep(hardwareLagg);															// compensate hardware-lagg
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			}																
			
			while (checkMovingRange() && !changeDirection && go) {									// sensor is in range...
				if (checkColor(colorWhite) && foundBlack) {											// Detected white surface after a black surface
					changeDirection = true;
				}
				else if (checkColor(colorBlack)) {													// Detected black surface
					foundBlack = true;
				}
				else {																				// Detected sth. other...
					/* ignore */
				}
			}
			
			reverseDirection();
		}
	}
	
	
	/**
	 * This will reverse the moving direction of the sensor.
	 */
	public static void reverseDirection() {
		direction = direction * -1;																	// reverse the moving-direction
		
		if (foundBlack) {																			// If black surface was found...
			blackBeforeReverse = true;																// ...found black before reverse -> true
			foundBlack = false;																		// ...in this direction nothing black was found (yet)
			reverseCounter = 0;																		// ...reset direction-change-counter
			
			if (getRealTachoCount() != lastPosition) {												// if new position isn't the same as last position...
				penultimatePosition = lastPosition;													// ...last got memorized
				lastPosition = getRealTachoCount();													// ...yeah, this position is now the last one
				
				calcLinePosition();																	// ...go, do it!
			}
		}
		else {																						// No black surface was found...
			reverseCounter++;																		// ...count that fail!
		}
	}
	
	
	/**
	 * Let the sensor do a left turn
	 */
	public static void turnLeft() {
		Motor.B.forward();
	}
	
	
	/**
	 * Let the sensor do a right turn
	 */
	public static void turnRight() {
		Motor.B.backward();
	}
	
	
	/**
	 * Check, if a given color is in the tolerance-range
	 * 
	 * @param paraColor color which should be checked
	 */
	private static boolean checkColor(int paraColor) {
		if (ls.readNormalizedValue() >= (paraColor - colorTolerance) &&								// yeah, it's the color!
			ls.readNormalizedValue() <= (paraColor + colorTolerance))
		{
			return true;
		}
		else {																						// nope..
			return false;
		}
	}
	
	
	/**
	 * Check, if the sensor is in the moving-range
	 */
	private static boolean checkMovingRange() {
		if (getRealTachoCount() > maxLeft + brakingCompensation &&									// sensor is in range
			getRealTachoCount() < maxRight - brakingCompensation)
		{
			return true;
		}
		else {																						// sensor isn't in range
			return false;
		}
	}
	
	
	/**
	 * returns the position of the black line (middle between two sensorpositions)
	 * 
	 * @param paraOne one site of the position
	 * @param paraTwo other site of the position
	 */
	private static void calcLinePosition() {
		linePosition = (int) Math.round(((penultimatePosition + lastPosition) / 2) * 1.5);			// average * sensorDegree-to-normalDegree
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
	public static void setSpeed(int paraSpeed) {
		sensorSpeed = paraSpeed;
		Motor.B.setSpeed(paraSpeed);
		brakingCompensation = Math.round((paraSpeed / 200) * 10);									// calculate some extra braking-distance
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
	 * 	x < 0	left
	 *  x = 0	center
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
		setColor(defaultColorWhite, defaultColorBlack, defaultColorTolerance);						// White, Black, Tol.
	}
	
	
	/**
	 * Set the default moving-range of th sensor
	 */
	public static void setDefaultMovement() {
		setMovement(defaultMaxLeft, defaultMaxRight);												// left, right
	}
	
	
	
	/* *****************************
	 * GETTER
	 * *****************************/
	
	/**
	 * Returns negated Motor-Tacho-Count (2-factor-gearing)
	 * 
	 * @return negated TachoCount
	 */
	private static int getRealTachoCount() {
		return Motor.B.getTachoCount() * -1;
	}
	
	
	public static int getLine() {
		return linePosition;
	}
	
	
	public static boolean isBlack() {
		return foundBlack;
	}

}
