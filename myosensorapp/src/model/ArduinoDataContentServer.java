/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author johnmcculloch
 *
 */
public class ArduinoDataContentServer extends Thread {

	//Interface
	
	/**
	 * Interface Consumer with data method
	 * When interface implemented will allow access to receive Arduino Data when subscribed
	 * @author johnmcculloch
	 *
	 */
	public interface Consumer{
		public void data(double A0, double A1);
	}
	
	//Instance Variables
	
	/**
	 * private static instance of ArduinoDataSubscribe class
	 */
	private static ArduinoDataContentServer instance = null;
	
	/**
	 * Arduino Model 
	 */
	public ArduinoDataModel arduinoDataModel;
	
	/**
	 * List of consumers
	 * Classes subscribed to recieve Arduino Data
	 */
	private List<Consumer> consumers;
	
	//Constructors
	
	/**
	 * Default Constructor
	 */
	private ArduinoDataContentServer() {
		this.consumers = new ArrayList<Consumer>();
		this.arduinoDataModel = new ArduinoDataModel();
		this.start();
	}
	
	//Getters and Setters
	
	/**
	 * Public getInstance
	 * @return
	 */
	public static synchronized ArduinoDataContentServer getInstance() {
		
		//check if an instance has been initialised if so return instance
		//if not initialise new ArduinoDataContentServe 
		if(instance==null){
			instance = new ArduinoDataContentServer();
		} 
		return instance;
	}
	
	//Methods
	/**
	 * Register Consumer to List<Consumer>
	 * @param c (Class)
	 */
	public void registerConsumer(Consumer c) {
		this.consumers.add(c);
	}
	
	/**
	 * Remove Consumer from List<Consumer>
	 * @param c (Class)
	 */
	public void unregisterConsumer(Consumer c) {
		this.consumers.remove(c);
	}

	/**
	 * Pass data to all Consumers
	 * A0 signal from Arduino
	 * A1 signal from Arduino
	 * @param data
	 */
	public void sendToConsumers(double A0, double A1) {
		
		try {
		
		//loop through all consumers and supply with A0 and A1 pin data)
		for(Consumer c: this.consumers) {
			c.data(A0,A1);
		}
		
		}catch(Exception e) {
			System.err.println("Error supplying consumer with data, error handled");
		}
		
		
	}
	
	

}
