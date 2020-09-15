/**
 * 
 */
package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.fazecast.jSerialComm.SerialPort;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author johnmcculloch
 *
 */
public class PortListModel {

	// Instance vars
	/**
	 * Port names observable list
	 */
	private ObservableList<String> portNamesObservableList;

	/**
	 * Port list used to populate list to pass to Toolbar
	 */
	private ObservableList<String> portList;

	// Constructors

	/**
	 * Default Constructor
	 */
	public PortListModel() {

		portNamesObservableList = FXCollections.observableArrayList();

		populatePortNamesList();
		// set PortNames to the portList which has collected names of current active
		// ports
		this.setPortNamesObservableList(portList);

	}

	// Getters and Setters

	/**
	 * @return the portNamesObservableList
	 */
	public ObservableList<String> getPortNamesObservableList() {
		return portNamesObservableList;
	}

	/**
	 * @param portNamesObservableList the portNamesObservableList to set
	 */
	public void setPortNamesObservableList(ObservableList<String> portNamesObservableList) {
		this.portNamesObservableList = portNamesObservableList;
	}

	// Methods

	/**
	 * Deserialize PortName Selected
	 * Will return null if file not located
	 * @return portName
	 */
	public String deserializePortName() {

		// Deserialization of portName
		try {
			
			String objectPortName;
			
			// Reading the object from a file
			File fileName = new File("serializedPortName", "portName");
			FileInputStream file = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(file);

			// Method for deserialization of object
			objectPortName = (String) in.readObject();

			// close resources to stop resource leak
			in.close();
			file.close();

			return objectPortName;
		}

		catch (IOException ex) {
		}

		catch (ClassNotFoundException ex) {
			//if file not found return null
			return null;
		}
		//if file not found return null
		return null;
	}

	/**
	 * Serializes PortName Selected (If selected)
	 * 
	 * @param portName
	 */
	public void serializePortName(String portName) {

		try {
			// checks if folder is created, if not will create a subfolder to store object
			File currentFolder = new File(".");
			File workingFolder = new File(currentFolder, "serializedPortName");
			if (!workingFolder.exists()) {
				workingFolder.mkdir();
			}

			// Serialization
			// Saving of object in a file
			File file = new File("serializedPortName", "portName");

			// if file does not exist create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// prepare file name to be written to
			FileOutputStream fileName = new FileOutputStream(file);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileName);

			// Method for serialisation of object
			objectOutputStream.writeObject(portName);

			// close resources to stop resource leak
			objectOutputStream.close();
			fileName.close();

		}

		catch (IOException ex) {
			System.err.println("IOException in PortList Model handeled");
		}

	}

	/**
	 * Populates ObervableList portList with names of current computer Ports
	 */
	public void populatePortNamesList() {

		try {

			// portList to store portNames
			portList = FXCollections.observableArrayList();

			// array to collect portNames from SerialPort library
			SerialPort[] portNames = SerialPort.getCommPorts();

			// populate portList list with portNames using system portName
			// systemPortName most recognisable name for user to read.
			for (SerialPort p : portNames) {
				portList.add(p.getSystemPortName());
			}
		this.setPortNamesObservableList(portList);

		} catch (Exception e) {

		}

	}

}
