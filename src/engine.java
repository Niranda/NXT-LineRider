

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
 * 	Bereitstellung grundlegender Funktionen der Antriebsmotoren
 */

public class engine {
	private static int speed;
	private static DifferentialPilot robot = new DifferentialPilot(5.5, 10.5, Motor.A, Motor.C); 	// Pilot-Objekt Erzeugung (Raddurchmesser cm, Abstand cm, Motor1, Motor2)
	
	
	public static void main(String[] args)throws Exception {
		Button.ENTER.waitForPressAndRelease();
		
		while(!Button.ESCAPE.isDown()) {
//			robot.rotate(180);
			turnLeft(90);
			Button.ENTER.waitForPressAndRelease();
		}
	}
	
	
	/**
	 * Move forward
	 */
	public static void forward() {
		robot.forward();
	}
	
	
	/**
	 * Move backward
	 */
	public static void backward() {
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
		//robot.steer(123);			// TODO interesting..!
	}
	
	
	/**
	 * Let it now how fast it should travel
	 * @param paraSpeed 10 is very slow, 300+ goes like hell!
	 */
	public static void setSpeed(int paraSpeed) {
		robot.setTravelSpeed(paraSpeed);
		speed = paraSpeed;
	}
	
}
