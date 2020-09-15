/**
 * 
 */
package model;

/**
 * @author johnmcculloch
 * This class is to build an object of the sensors recorded
 */
public class ArduinoSignalRecord {

	//Instance variables
	/**
	 * Sensor One X Axis Counter 
	 */
	private int sensorOneXAxisCounter;

	/**
	 * Sensor Two X Axis Counter
	 */
	private int sensorTwoXAxisCounter;
	
	/**
	 * Recorded Signal from pin A0 
	 */
	private  double recordedSignalA0; 

	/**
	 * Recorded Signal from pin A1
	 */
	private  double recordedSignalA1; 
	
	//Constructors
	
	/**
	 * Default Constructor
	 */
	public ArduinoSignalRecord() {
		
	}
	
	/**
	 * @param sensorOneXAxisCounter
	 * @param sensorTwoXAxisCounter
	 * @param recordedSignalA0
	 * @param recordedSignalA1
	 */
	public ArduinoSignalRecord(int sensorOneXAxisCounter, int sensorTwoXAxisCounter, double recordedSignalA0,
			double recordedSignalA1) {
		super();
		this.sensorOneXAxisCounter = sensorOneXAxisCounter;
		this.sensorTwoXAxisCounter = sensorTwoXAxisCounter;
		this.recordedSignalA0 = recordedSignalA0;
		this.recordedSignalA1 = recordedSignalA1;
	}
	
	//Getters and Setters
	
	/**
	 * @return the sensorOneXAxisCounter
	 */
	public int getSensorOneXAxisCounter() {
		return sensorOneXAxisCounter;
	}

	/**
	 * @param sensorOneXAxisCounter the sensorOneXAxisCounter to set
	 */
	public void setSensorOneXAxisCounter(int sensorOneXAxisCounter) {
		this.sensorOneXAxisCounter = sensorOneXAxisCounter;
	}

	/**
	 * @return the sensorTwoXAxisCounter
	 */
	public int getSensorTwoXAxisCounter() {
		return sensorTwoXAxisCounter;
	}

	/**
	 * @param sensorTwoXAxisCounter the sensorTwoXAxisCounter to set
	 */
	public void setSensorTwoXAxisCounter(int sensorTwoXAxisCounter) {
		this.sensorTwoXAxisCounter = sensorTwoXAxisCounter;
	}

	/**
	 * @return the recordedSignalA0
	 */
	public double getRecordedSignalA0() {
		return recordedSignalA0;
	}

	/**
	 * @param recordedSignalA0 the recordedSignalA0 to set
	 */
	public void setRecordedSignalA0(double recordedSignalA0) {
		this.recordedSignalA0 = recordedSignalA0;
	}

	/**
	 * @return the recordedSignalA1
	 */
	public double getRecordedSignalA1() {
		return recordedSignalA1;
	}

	/**
	 * @param recordedSignalA1 the recordedSignalA1 to set
	 */
	public void setRecordedSignalA1(double recordedSignalA1) {
		this.recordedSignalA1 = recordedSignalA1;
	}


}
