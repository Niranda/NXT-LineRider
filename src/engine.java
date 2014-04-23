

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

/*
 * Description
 * 	Basic motor/engine functions
 */

public class engine extends Thread {
	static dataExchange de;
	
	private static int speed;
	private static DifferentialPilot robot = new DifferentialPilot(5.5, 10, Motor.A, Motor.C); 	// Pilot-Objekt Erzeugung (Raddurchmesser cm, Abstand cm, Motor1, Motor2)
	
	
	public static void main(String[] args)throws Exception {
		
	}
	
	
	/**
	 * Constructor
	 * 
	 * @param paraDe we need an data exchanger!
	 */
	public engine (dataExchange dataExchange) {
		de = dataExchange;
	}
	
	
	public void run() {
		forward();
		
		while(isMoving()) {
			steer(de.getLinePosition());
			
			if (Button.ENTER.isDown()) {
				break;
			}
		}
	}
	
	
	public void stop() {
		robot.stop();
	}
	
	public void stop(boolean hardStop) {
		if (hardStop) {
			robot.quickStop();
		}
		else {
			stop();
		}
	}
	
	
	/**
	 * Move forward
	 */
	public static void forward() {
		robot.forward();
	}
	
	public void forward(int travelSpeed) {
		setSpeed(travelSpeed);
		robot.forward();
	}
	
	
	/**
	 * Move backward
	 */
	public static void backward() {
		robot.backward();
	}
	
	public void backward(int travelSpeed) {
		setSpeed(travelSpeed);
		robot.backward();
	}
	
	
	/**
	 * let the robot do a right-turn
	 */
	public static void turnRight() {
		robot.rotateLeft();
	}
	
	/**
	 * @param degree the degree
	 */
	public static void turnRight(int degree) {
		if (degree < 0) {												// only right
			degree = degree * -1;
		}
		
		robot.rotate(degree);
	}
	
	
	/**
	 * let the robot do a left-turn
	 */
	public static void turnLeft() {
		robot.rotateRight();
	}

	
	/**
	 * @param degree the degree
	 */
	public static void turnLeft(int degree) {
		if (degree > 0) {												// only left
			degree = degree * -1;
		}
		
		robot.rotate(degree);
	}
	
	
	/**
	 * let the robot turn in x degrees
	 * x < 0 --> left turn
	 * x > 0 --> right turn
	 * 
	 * @param degree degree
	 */
	public static void rotate(int degree) {
		robot.rotate(degree);
	}
	
	
	/**
	 * Starts the robot moving forward along a curved path. This method is similar to the arcForward(double radius) method
	 * except it uses the turnRate parameter do determine the curvature of the path and therefore has the ability to drive
	 * straight. This makes it useful for line following applications. 
	 * The turnRate specifies the sharpness of the turn. Use values between -200 and +200.
	 * A positive value means that center of the turn is on the left. If the robot is traveling toward the top of the page
	 * the arc looks like this: ). 
	 * A negative value means that center of the turn is on the right so the arc looks this: (.
	 * . In this class, this parameter determines the ratio of inner wheel speed to outer wheel speed as a percent.
	 * Formula: ratio = 100 - abs(steerPercent).
	 * When the ratio is negative, the outer and inner wheels rotate in opposite directions. Examples of how the formula
	 * works: 
	 * 
	 * steerPercent(0)	 -> inner and outer wheels turn at the same speed, travel straight 
	 * steerPercent(25)  -> the inner wheel turns at 75% of the speed of the outer wheel, turn left 
	 * steerPercent(100) -> the inner wheel stops and the outer wheel is at 100 percent, turn left 
	 * steerPercent(200) -> the inner wheel turns at the same speed as the outer wheel - a zero radius turn. 
	 * Note: If you have specified a drift correction in the constructor it will not be applied in this method.
	 * 
	 * @param steerPercent If positive, the left side of the robot is on the inside of the turn. If negative, the left side is on the outside.
	 */
	public static void steer(double steerPercent) {
		robot.steer(steerPercent);
	}
	
	
	/**
	 * Let it know how fast it should travel
	 * @param paraSpeed 10 is very slow, 300+ goes like hell!
	 */
	public static void setSpeed(int paraSpeed) {
		robot.setTravelSpeed(paraSpeed);
		de.setEngineSpeed(paraSpeed);
	}
	
	
	public static boolean isMoving() {
		return robot.isMoving();
	}
	
}
