
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

/* *****************************
 * Description
 * 	Basic functions of the robot's moving sensor in the front
 *  (180 degree swiveling)
 * *****************************/

public class movingSensor extends Thread {
	
/* *****************************
 * VARIABLE DECLARATION
 * *****************************/
	
	/*
	 * OBJECTS
	 */
	private static movingSensor ms = new movingSensor(new dataExchange());
	private sfx sfx = new sfx();
	
	private dataExchange de;
	private LightSensor ls = new LightSensor(SensorPort.S1);

	private boolean loc_beforeReverseWasBlack = false;
	private boolean loc_beforeReverseWasWhite = false;
	private boolean loc_lastReadWasBlack = false;
	private boolean loc_lastReadWasWhite = false;
	
	private boolean loc_reverse = false;
	
	
	
	
	
	
/* *****************************
 * BASIC METHODS
 * *****************************/
	
	/**
	 * main method
	 */
	public static void main(String[] args)throws Exception  {
		LCD.drawString("SENSOR TESTMODE", 1, 1);
		LCD.drawString("Hit ENTER + ESC to stop", 1, 2);
		
		ms.loadDefault();
		ms.followLine();
		
	}
	
	/**
	 * Constructor, catch the data exchanger
	 * 
	 * @param paraDe we need an data exchanger!
	 */
	public movingSensor (dataExchange dataExchange) {
		de = dataExchange;
	}
	
	/**
	 * Start-method of multithreading
	 */
	public void run() {
		// TODO: initialize and maybe some changes to start
	}
	
	/**
	 * Stop that thread (may not the correct way)
	 */
	public void stop() {
		// TODO: check, if "go" has not changed
	}
	
	
	
	
	
/* *****************************
 * OPERATION METHODS
 * *****************************/
	/**
	 * Initialize the sensor (before start!)
	 * The sensor has to be at middle position (0 degrees)
	 * and on a black line!
	 */
	public void initialize() {
		engineResetTachoCount();												// middle ^= 0 degrees
		setEngineSpeed(20);
		
		de.setInitSensorColorBlack(getSensorValue());							// read current black value
		sfx.beep();
		
		engineTurnLeft();														// turn to max left
		while(engineIsInRange()) {}
		engineStop();
	
		if (getSensorValue() > de.getInitSensorColorBlack() + de.getSensorColorTolerance()) {
			de.setInitSensorColorWhite(getSensorValue());						// read white
		}
	
		sfx.beep(2);
		engineTurnRight();														// turn to max right
		while(engineIsInRange()) {}
		engineStop();
		
		sfx.beep(2);
		if (getSensorValue() > de.getInitSensorColorBlack() + de.getSensorColorTolerance()) {
			de.setInitSensorColorWhite(getSensorValue());						// read white
		}
		engineTurnTo(0);														// turn back to middle
		
		setEngineSpeed(75);
		
		sfx.beep();
	}
	
	public void loadDefault() {
		de.setInitSensorColorWhite(550);
		de.setInitSensorColorBlack(350);
		de.setSensorColorTolerance(10);
		setEngineSpeed(200);
	}
	
	
	public void followLine() {
//		de.setSensorState(1); // TODO test
		
		while (de.getSensorActive() /*|| true*/) {											// if sensor active -> LF line
			if (sensorIsBlack()) {												// get current color
				loc_lastReadWasBlack = true;
			}
			else if (sensorIsWhite()) {
				loc_lastReadWasWhite = true;
			}
			else {
				loc_lastReadWasBlack = false;
				loc_lastReadWasWhite = false;
			}
			
			
			switch (de.getSensorState()) {										// check next direction
				case -1:
					engineTurnLeft();
					break;
					
				case 1:
					engineTurnRight();
					break;
					
				default:														// sth. went wrong -> stop!
					sfx.beep();
					break;
			}
			
			
			while (	engineIsMoving() &&											// as long as: 	... engine is moving
					de.getSensorActive() &&										//				... sensor is turned on
					engineIsInRange() &&										//				... engine is moving in range
					!loc_reverse)												//				... engine should NOT reverse
			{

				if (sensorIsBlack() && loc_lastReadWasBlack) {					// no colorchange (black)
					
				}
				else if (sensorIsWhite() && loc_lastReadWasWhite) {				// no colorchange (white)
					
				}
				else {															// color changed!
					loc_reverse = true;											// reverse direction
					
					loc_beforeReverseWasBlack = loc_lastReadWasBlack;			// remember last colors
					loc_beforeReverseWasWhite = loc_lastReadWasWhite;
				}
			}
			
			engineReverse();
		}
		
		engineTurnTo(0);														// At any end: turn back to normal position
	}
	
	
	
	
	/*
	 * GENERAL CONTROLS
	 */
	
	
	
	/*
	 * ENGINE CONTROLS 
	 */
	
	/**
	 * True, if the sensor is in range
	 * 
	 * New: Now it only checks one specific max at moving
	 */
	public boolean engineIsInRange() {
		switch (de.getLiveSensorEngineState()) {
			case (1):															// on right-turn
				if (getEngineRealTachoCount() <= de.getSensorRangeMaxRight()) {
					return true;
				}
				else {
					return false;
				}
			
			case (-1):															// on left-turn
				if (getEngineRealTachoCount() >= de.getSensorRangeMaxLeft()) {
					return true;
				}
				else {
					return false;
				}
			
			default:
				if (getEngineRealTachoCount() <= de.getSensorRangeMaxRight() &&
					getEngineRealTachoCount() >= de.getSensorRangeMaxLeft()) {
					return true;
				}
				else {
					return false;
				}
		}
	}
	
	public void engineReverse() {
		engineStop();
		de.setSensorState(de.getSensorState() * -1);
		loc_reverse = false;
	}
	
	public void engineTurnTo(int degree) {
		if (degree >= de.getSensorRangeMaxLeft() ||								// check, if turnTo degree is in range
			degree <= de.getSensorRangeMaxRight()) {							// it is..
		
			if (getEngineRealTachoCount() < degree) {							// sensor is somewhere left from target
				engineTurnRight();
				
				while (engineIsInRange() && getEngineRealTachoCount() < degree) {}
			}
			else if (getEngineRealTachoCount() > degree) {						// sensor is somewhere right from target
				engineTurnLeft();
				
				while (engineIsInRange() && getEngineRealTachoCount() > degree) {}
			}
			
			engineStop();
			
		}
	}
	
	/**
	 * reset TachoCount
	 */
	private void engineResetTachoCount() {
		Motor.B.resetTachoCount();
	}
	
	/**
	 * Let the sensor do a left turn
	 */
	public void engineTurnLeft() {
		de.setSensorActive(true);
		de.setLiveSensorEngineState(-1);
		Motor.B.forward();
	}
	
	/**
	 * Let the sensor do a right turn
	 */
	public void engineTurnRight() {
		de.setSensorActive(true);
		de.setLiveSensorEngineState(1);
		Motor.B.backward();
	}
	
	/**
	 * Stop moving
	 */
	public void engineStop() {
		Motor.B.stop();
		de.setSensorActive(false);
		de.setLiveSensorEngineState(0);
	}
	
	/**
	 * True, if the sensor-engine is moving
	 */
	public boolean engineIsMoving() {
		return Motor.B.isMoving();
	}
	
	
	
	/*
	 * SENSOR CONTROLS
	 */
	public void sensorRead() {
		sensorIsBlack();
		sensorIsWhite();
	}
	
	public boolean sensorIsBlack() {
		if (getSensorValue() - de.getSensorColorTolerance() <= de.getInitSensorColorBlack()) {		// current color - tolerance is lower than black...
			de.setLiveSensorColorIsBlack(true);
		}
		else {
			de.setLiveSensorColorIsBlack(false);
		}
		
		return de.getLiveSensorColorIsBlack();
	}
	
	public boolean sensorIsWhite() {
		if (getSensorValue() + de.getSensorColorTolerance() >= de.getInitSensorColorWhite()) {		// current color + tolerance is larger than white...
			de.setLiveSensorColorIsWhite(true);
		}
		else {
			de.setLiveSensorColorIsWhite(false);
		}
		
		return de.getLiveSensorColorIsWhite();
	}
	
	
	
	
	
/* *****************************
 * SETTER METHODS
 * *****************************/
	
	/*
	 * ENGINE SETTER
	 */
	public void setEngineSpeed(int speed) {
		de.setSensorSpeed(speed);
		Motor.B.setSpeed(speed);
	}
	
	
	
	
	
/* *****************************
 * GETTER METHODS
 * *****************************/
	
	/*
	 * ENGINE GETTER
	 */
	
	/**
	 * Returns negated Motor-Tacho-Count (2-factor-gearing)
	 * 
	 * @return negated TachoCount
	 */
	public int getEngineRealTachoCount() {
		return Motor.B.getTachoCount() * -1;
	}
	
	
	/*
	 * SENSOR GETTER
	 */
	public int getSensorValue() {
		return ls.readNormalizedValue();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	/* *****************************
//	 * VARIABLE DECLARATION
//	 * *****************************/
//	static dataExchange de;
//	
//	private static int colorWhite;																// Wert auf weißen Oberflächen
//	private static int colorBlack;																// Wert auf schwarzen Oberflächen
//	private static int colorTolerance;															// Toleranz in Einheiten
//	
//	private static int maxLeft;																	// maximale Sensorstellung: links
//	private static int maxRight;																// maximale Sensorstellung: rechts
//	
//	private static int linePosition = 0;														// line's position in normal degree
//	private static int lastPosition = 0;														// last seen position
//	private static int penultimatePosition = 0;													// penultimated position (last memorized)
//	
//	private static boolean 	go = true;															// TRUE = arbeitend, FALSE = nicht arbeitend
//	private static int 		direction = 1;														// Sensor Bewegungsrichtung (1 = right, -1 = left)
//	private static int 		lastDirection;														// Direction before change
//	private static boolean 	changeDirection = false;											// true, if direction-change is incoming (while-break)
//	private static boolean 	blackBeforeReverse = false;											// true, if before a reverse was black found, false if not
//	private static boolean 	foundBlack = false;													// set true, if black was found (just reverse set it false)
//	private static int 		reverseCounter = 0;													// reverse-counter, if there wasn't a black-found before
//	
//	private static int hardwareLagg = 100;														// Thread sleeping for ...ms to compensate the hardware-lagg
//	private static int sensorSpeed = 50;														// speed of the sensor, calculates braking-distance
//	private static int brakingCompensation;														// the faster the sensor, the higher the braking-distance - a balancer
//	
//	
//	/*
//	 * OBJECTS
//	 */
//	private static LightSensor ls = new LightSensor(SensorPort.S1);								// LightSensor initiieren
//	
//	
//	/*
//	 * DEFAULTS
//	 */
//	private static int defaultColorWhite = 550;
//	private static int defaultColorBlack = 400;
//	private static int defaultColorTolerance = 25;
//	
//	private static int defaultMaxLeft = -60;													// 1 sensor-degree == 1,5 degree
//	private static int defaultMaxRight = 60;
//	
//
//	
//	
//
//	
//	
//	/**
//	 * Sensor will find and follow the black line.
//	 * The variable 'position' will be set to the current position of the sensor.
//	 * 
//	 * At start the sensor has to be in center-position!
//	 * 
//	 * @throws InterruptedException 
//	 */
//	public static void searchLine() {
//		// TODO: add here the new concept
//		Motor.B.resetTachoCount();																	// 0 degree at start :-)
//		
//		setSpeed(sensorSpeed);																		// set some values..
//		setDefaultColor();
//		setDefaultMovement();
//		
//		while(go) {																					// start moving!
//			changeDirection = false;
//			
//			if (direction == 1) {																	// turn right
//				turnRight();
//			}
//			else if (direction == -1) {																// turn left
//				turnLeft();
//			}
//
//			try {																					// workaround for multithreading
//				Thread.sleep(hardwareLagg);															// compensate hardware-lagg
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//			}																
//			
//			while (checkMovingRange() && !changeDirection && go) {									// sensor is in range...
//				if (checkForWhite() && foundBlack) {												// Detected white surface after a black surface
//					changeDirection = true;
//				}
//				else if (checkForBlack()) {															// Detected black surface
//					foundBlack = true;
//				}
//				else {																				// Detected sth. other...
//					/* ignore */
//				}
//			}
//			
//			reverseDirection();
//		}
//	}
//	
//	
//	/**
//	 * This will reverse the moving direction of the sensor.
//	 */
//	public static void reverseDirection() {
//		// TODO: IMPORTANT! need a new concept
//		direction = direction * -1;																	// reverse the moving-direction
//		
//		if (foundBlack) {																			// If black surface was found...
//			blackBeforeReverse = true;																// ...found black before reverse -> true
//			foundBlack = false;																		// ...in this direction nothing black was found (yet)
//			reverseCounter = 0;																		// ...reset direction-change-counter
//			
//			if (getRealTachoCount() != lastPosition) {												// if new position isn't the same as last position...
//				penultimatePosition = lastPosition;													// ...last got memorized
//				lastPosition = getRealTachoCount();													// ...yeah, this position is now the last one
//				
//				setLinePosition();																	// ...go, do it!
//			}
//		}
//		else {																						// No black surface was found...
//			reverseCounter++;																		// ...count that fail!
//		}
//	}
//	
//	
//	
//	
//	
//	/**
//	 * True, if the surface is like white.
//	 * 
//	 * @return
//	 */
//	private static boolean checkForWhite() {
//		LCD.drawInt(ls.readNormalizedValue(), 1, 5);
//		if (ls.readNormalizedValue() >= colorWhite) {
//			return true;
//		}
//		else {
//			return false;
//		}
//	}
//	
//	
//	/**
//	 * True, if the surface is like black.
//	 * 
//	 * @return
//	 */
//	private static boolean checkForBlack() {
//		LCD.drawInt(ls.readNormalizedValue(), 1, 5);
//		if (ls.readNormalizedValue() <= colorBlack) {
//			return true;
//		}
//		else {
//			return false;
//		}
//	}
//	
//	
//	/**
//	 * Check, if the sensor is in the moving-range
//	 */
//	private static boolean checkMovingRange() {
//		if (getRealTachoCount() > maxLeft + brakingCompensation &&									// sensor is in range
//			getRealTachoCount() < maxRight - brakingCompensation)
//		{
//			return true;
//		}
//		else {																						// sensor isn't in range
//			return false;
//		}
//	}
//
//	
//	private static void setLinePosition() {
//		linePosition = (int) Math.round(((penultimatePosition + lastPosition) / 2) * 1.5);			// average * sensorDegree-to-normalDegree
//		de.setLinePosition(linePosition);	
//	}
//	
//	
//	
//	/* *****************************
//	 * SETTER
//	 * *****************************/
//	
//	/**
//	 * Set the moving-speed of the sensor and calculate some extra space for
//	 * an extra breaking-range
//	 * 
//	 * @param paraSpeed
//	 */
//	public static void setSpeed(int paraSpeed) {
//		sensorSpeed = paraSpeed;
//		Motor.B.setSpeed(paraSpeed);
//		brakingCompensation = Math.round((paraSpeed / 200) * 10);									// calculate some extra braking-distance
//	}
//	
//	/**
//	 * set color-reflections
//	 *  all parameters are in 'normalized values' (not percentages!)
//	 * 
//	 * @param valueWhite reflection of white surfaces
//	 * @param valueBlack reflection of black surfaces
//	 * @param valueTolerance tolerance range
//	 */
//	public static void setColor(int valueWhite, int valueBlack, int valueTolerance) {
//		colorWhite = valueWhite;
//		colorBlack = valueBlack;
//		colorTolerance = valueTolerance;
//	}
//	
//	
//	/**
//	 * set sensor's "freedom of movement"
//	 * 	x < 0	left
//	 *  x = 0	center
//	 *  x > 0	right
//	 * 
//	 * @param paraLeft maximum in the left (value lower then 0)
//	 * @param paraRight maximum in the right (value higher then 0)
//	 */
//	public static void setMovement(int paraLeft, int paraRight) {
//		maxLeft = paraLeft;
//		maxRight = paraRight;
//	}
//	
//
//	/**
//	 * Set the default values of color-reflections
//	 */
//	public static void setDefaultColor() {
//		setColor(defaultColorWhite, defaultColorBlack, defaultColorTolerance);						// White, Black, Tol.
//	}
//	
//	
//	/**
//	 * Set the default moving-range of th sensor
//	 */
//	public static void setDefaultMovement() {
//		setMovement(defaultMaxLeft, defaultMaxRight);												// left, right
//	}
//	
//	
//	
//	/* *****************************
//	 * GETTER
//	 * *****************************/
//	
//	
//	public static int getLine() {
//		return linePosition;
//	}
//	
//	
//	public static boolean isBlack() {																// srsly dude?
//		return foundBlack;
//	}

}
