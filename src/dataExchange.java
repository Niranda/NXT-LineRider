
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
	
/* *****************************
 * VARIABLE DECLARATION
 * *****************************/
	
	/*
	 * ENGINE VARIABLES
	 */
	private static int 		engineSpeed = 10;
	
	/*
	 * SENSOR VARIABLES
	 */
	private static int 		sensorSpeed = 50;
	private static int 		sensorState = 0;						// target sensor state: -1 left, 0 stop, 1 right
	private static int 		sensorRangeMaxLeft = -60;
	private static int 		sensorRangeMaxRight = 60;
	private static int 		sensorColorTolerance = 20;
	private static boolean 	sensorActive = false;
	
	/*
	 * LIVE VARIABLES
	 */
	private static int 		liveLinePosition = 0;
	private static int 		liveSensorEngineTachoCount = 0;
	private static int 		liveSensorEngineState = 0;				// -1 left-turn, 0 no turn, 1 right-turn
	private static boolean 	liveSensorColorIsBlack = false;			// is current color black?
	private static boolean 	liveSensorColorIsWhite = false;			// is current color black?
	
	/*
	 * INITIALIZED VARIABLES
	 */
	private static int 		initSensorColorBlack;
	private static int 		initSensorColorWhite;
	
	/*
	 * ERROR VARIABLES
	 */
	private static boolean	errorState = false;						// is there an error?!
	private static String	errorMsg;								// Error Message
	private static String 	errorFile;								// File with error
	private static int		errorLine;								// Error in line
	
	
	
/* *****************************
 * BASIC METHODS
 * *****************************/
	
	/**
	 * main method
	 */
	public static void main(String[] args)throws Exception  {
		
	}
	
	
	/**
	 * Constructor
	 */
	public dataExchange () {
		
	}
	
	
	/**
	 * Check state variable
	 * All values lower then 0 will be -1
	 * All value higher then 0 will be 1
	 * All others will be 0
	 * 
	 * @param speed
	 */
	public int checkState (int state) {
		if (state < 0) {
			return -1;
		}
		else if (state > 0) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
	
/* *****************************
 * SETTER METHODS
 * *****************************/
	
	/*
	 * ENGINE SETTER
	 */
	public void setEngineSpeed(int speed) {
		engineSpeed = speed;
	}
	
	
	/*
	 * SENSOR SETTER
	 */
	public void setSensorSpeed(int speed) {
		sensorSpeed = speed;
	}
	
	public void setSensorState(int state) {
		sensorState = checkState(state);
	}
	
	public void setSensorRangeMaxLeft(int maxLeft) {
		sensorRangeMaxLeft = maxLeft;
	}
	
	public void setSensorRangeMaxRight(int maxRight) {
		sensorRangeMaxRight = maxRight;
	}
	
	public void setSensorColorTolerance(int tolerance) {
		sensorColorTolerance = tolerance;
	}
	
	public void setSensorActive(boolean state) {
		sensorActive = state;
	}
	
	
	/*
	 * LIVE SETTER
	 */
	public void setLiveLinePosition(int linePosi) {
		liveLinePosition = linePosi;
	}
	
	public void setLiveSensorEngineTachoCount(int tachoCount) {
		liveSensorEngineTachoCount = tachoCount;
	}
	
	public void setLiveSensorEngineState(int state) {
		state = checkState(state);
		
		if (state != 0) {
			sensorActive = true;
		}
		else {
			sensorActive = false;
		}
	}
	
	public void setLiveSensorColorIsBlack() {
		setLiveSensorColorIsBlack(true);
	}
	
	public void setLiveSensorColorIsBlack(boolean isBlack) {
		liveSensorColorIsBlack = isBlack;
	}
	
	public void setLiveSensorColorIsWhite(boolean isWhite) {
		liveSensorColorIsBlack = isWhite;
	}
	
	
	
	/*
	 * INITIALIZE SETTER
	 */
	public void setInitSensorColorBlack(int black) {
		initSensorColorBlack = black;
	}
	
	public void setInitSensorColorWhite(int white) {
		initSensorColorWhite = white;
	}
	
	
	/*
	 * ERROR SETTER
	 */
	public void setErrorState(boolean state) {
		errorState = state;
	}
	
	public void setErrorMsg(String msg) {
		errorMsg = msg;
	}
	
	public void setErrorFile(String file) {
		errorFile = file;
	}
	
	public void setErrorLine(int line) {
		errorLine = line;
	}
	
	
	
/* *****************************
 * MULTI-SETTER METHODS
 * *****************************/
	
	/*
	 * SENSOR MULTI-SETTER
	 */
	public void setSensorRangeMax(int bothSides) {
		setSensorRangeMax(bothSides, bothSides);
	}
	
	public void setSensorRangeMax(int left, int right) {
		setSensorRangeMaxLeft(left);
		setSensorRangeMaxRight(right);
	}
	
	
	/*
	 * ERROR MULTI-SETTER
	 */
	
	/**
	 * Set an error
	 * 
	 * @param errMsg Message
	 * @param errFile File
	 * @param errLine line
	 */
	public void setError(String errMsg, String errFile, int errLine) {
		setErrorState(true);
		setErrorMsg(errMsg);
		setErrorFile(errFile);
		setErrorLine(errLine);
	}
	
	
	
/* *****************************
 * GETTER METHODS
 * *****************************/
	
	/*
	 * ENGINE GETTER
	 */
	public int getEngineSpeed() {
		return engineSpeed;
	}
	
	
	/*
	 * SENSOR GETTER
	 */
	public int getSensorSpeed() {
		return sensorSpeed;
	}
	
	public int getSensorState() {
		return sensorState;
	}
	
	public int getSensorRangeMaxLeft() {
		return sensorRangeMaxLeft;
	}
	
	public int getSensorRangeMaxRight() {
		return sensorRangeMaxRight;
	}
	
	public int getSensorColorTolerance() {
		return sensorColorTolerance;
	}
	
	public boolean getSensorActive() {
		return sensorActive;
	}
	
	
	/*
	 * LIVE GETTER
	 */
	public int getLiveLinePosition() {
		return liveLinePosition;
	}
	
	public int getLiveSensorEngineTachoCount() {
		return liveSensorEngineTachoCount;
	}
	
	public int getLiveSensorEngineState() {
		return liveSensorEngineState;
	}
	
	public boolean getLiveSensorColorIsBlack() {
		return liveSensorColorIsBlack;
	}
	
	public boolean getLiveSensorColorIsWhite() {
		return liveSensorColorIsWhite;
	}
	
	
	/*
	 * INITIALIZE GETTER
	 */
	public int getInitSensorColorBlack() {
		return initSensorColorBlack;
	}
	
	public int getInitSensorColorWhite() {
		return initSensorColorWhite;
	}

	
	/*
	 * ERROR GETTER
	 */
	public boolean getErrorState() {
		return errorState;
	}
	
}
