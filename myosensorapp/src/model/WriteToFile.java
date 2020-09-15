/**
 * 
 */
package model;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import javafx.scene.chart.XYChart;

/**
 * @author johnmcculloch
 *
 */
public class WriteToFile {

	// Instant Variables
	/**
	 * User Details
	 */
	private model.UserDetails userDetails;

	/**
	 * File Name (Write to File)
	 */
	private String fileToWriteTo;


	/**
	 * Array List to store signal recordings
	 */
	private ArrayList<ArduinoSignalRecord> dataToWrite;

	// Constructors

	/**
	 * Default Constructor
	 */
	public WriteToFile() {
		dataToWrite = new ArrayList<ArduinoSignalRecord>();
	}

	// Getters and Setters

	/**
	 * @return the fileToWriteTo
	 */
	public String getFileToWriteTo() {
		return fileToWriteTo;
	}

	/**
	 * @param fileToWriteTo the fileToWriteTo to set
	 */
	public void setFileToWriteTo(String fileToWriteTo) {
		this.fileToWriteTo = fileToWriteTo;
	}
	
	/**
	 * @return the userDetails
	 */
	public model.UserDetails getUserDetails() {
		return userDetails;
	}

	/**
	 * @param userDetails the userDetails to set
	 */
	public void setUserDetails(model.UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	// Methods

	/**
	 * Collects data from Arduino and stores to String Used to print when finished
	 * to file
	 * 
	 * @param sensorOne
	 * @param sensorTwo
	 */
	public void collectArduinoDataForFile(XYChart.Data<Number, Number> sensorOne,
			XYChart.Data<Number, Number> sensorTwo) {

		int sensorOneXAxisCounter = sensorOne.getXValue().intValue();
		Double passVoltOne = sensorOne.getYValue().doubleValue();

		int sensorTwoXAxisCounter = sensorTwo.getXValue().intValue();
		Double passVoltTwo = sensorTwo.getYValue().doubleValue();

		dataToWrite.add(new ArduinoSignalRecord(sensorOneXAxisCounter, sensorTwoXAxisCounter, passVoltOne, passVoltTwo));
		//dataToWrite.add(new ArduinoSignalRecord(sensorOneXAxisCounter, sensorTwoXAxisCounter, passVoltOne, passVoltTwo));
		
	}

	/**
	 * Write to File UserDetailsw
	 * 
	 * @param UserDetails   String
	 * @param fileToWriteTo String
	 */
	public void writeDetailsToFile() {

		//create json object to convert Java object to json object to write to file 
		Gson json = new Gson();
		
		//convert the dataToWrite into a JsonArray object to be stored within the userDetails object
		JsonArray dataToWriteJsonArray = json.toJsonTree(dataToWrite).getAsJsonArray();
		
		//store the data collected JSON Array String into this class user details
		//dataToWriteJsonArray may be null catch exception if null
		//will be triggered on closing app
		try {
		this.userDetails.setDataCollected(dataToWriteJsonArray);
		}catch(Exception e) {}
		
		if (fileToWriteTo == null) {
			//do nothing as there is no file to write to
		} else {

			try {
				
				// checks if folder is created, if not will create a subfolder to store files
				//use file to write to name the folder
				File currentFolder = new File(".");
				File workingFolder = new File(currentFolder, this.fileToWriteTo);
				if (!workingFolder.exists()) {
					workingFolder.mkdir();
				}
				
				
				// Write all data to file using passed name of file with .json extension
				File fileJson = new File(this.fileToWriteTo, this.fileToWriteTo+".json");
				// Write data collected to file using passed name of file with .csv extension
				File fileCSV = new File(this.fileToWriteTo, this.fileToWriteTo+".csv");
				//Write user details into text file using passed name of file with .txt extension
				File fileTxt = new File(this.fileToWriteTo, this.fileToWriteTo+".txt");
				
				// if file does not exist write to new file
				// patientID and Date will ensure new file name no-duplicates
				if (!fileJson.exists()) {
					fileJson.createNewFile();
				}

				// Instantiate FileWriter
				// FileWriter - true or false (write over existing file or append to file)
				FileWriter fwJSON = new FileWriter(fileJson, false);
				
				//write userDetails to JSON file
				json.toJson(userDetails, fwJSON);
				
				//Now to set up and write data for csv file using file writer (append to file as loop through data) and buffered writer 
				FileWriter fwCSV = new FileWriter(fileCSV, true);
				BufferedWriter bwCSV = new BufferedWriter(fwCSV);
			
				bwCSV.write("Time(Ms),SignalA0,SignalA1\n");
				
				for(ArduinoSignalRecord data: this.dataToWrite) {
					
					bwCSV.write(data.getSensorOneXAxisCounter()+","+data.getRecordedSignalA0()+","+data.getRecordedSignalA1()+"\n");
					
				}
				
				//Now to set up and write user details to text file using file writer and buffered writer
				FileWriter fwTxt = new FileWriter(fileTxt, true);
				BufferedWriter bwTxt = new BufferedWriter(fwTxt);
				
				bwTxt.write(userDetails.printUserDetails());
				
				// close resources (stop resource leak)
				bwCSV.close();
				bwTxt.close();
				fwJSON.close();
				fwCSV.close();
				fwTxt.close();
				
			} catch (IOException e) {
				System.err.println("Error WriteToFile, error handeled");
				e.printStackTrace();
			}

		}
	}

}
