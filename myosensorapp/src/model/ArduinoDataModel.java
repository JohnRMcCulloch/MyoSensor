/**
 * 
 */
package model;

import java.text.DecimalFormat;
import java.util.Scanner;
import com.fazecast.jSerialComm.SerialPort;

import controller.LineGraphListener;
import controller.ToolbarLabelTextListener;
import controller.ToolbarVoltageListener;
import javafx.scene.chart.XYChart;

/**
 * @author johnmcculloch
 * Arduino Data Model class takes charge of incoming Arduino Data and supplying the controller with information.
 * Arduino Data Model publishes data to content server class ArduinoDataSubscribe
 */
public class ArduinoDataModel {

	/**
	 * Chosen Serial Port
	 */
	static SerialPort chosenPort;

	/**
	 * Chosen Port Name
	 */
	private String choenPortName;

	/**
	 * Toolbar button text (user is connected or disconnected)
	 */
	private String buttonText;

	/**
	 * lineGraphListener
	 */
	private LineGraphListener lineGraphListener;

	/**
	 * Toolbar label text listener
	 */
	private ToolbarLabelTextListener toolbarLabelTextListener;

	/**
	 * Toolbar Voltage Listener
	 */
	private ToolbarVoltageListener toolbarVoltageListener;

	/**
	 * Sensor One X Axis Counter (Used by Graph)
	 */
	static int sensorOneXAxisCounter = 0;

	/**
	 * Sensor Two X Axis Counter (Used by Graph)
	 */
	static int sensorTwoXAxisCounter = 0;

	// Constructors

	/**
	 * Default Constructors
	 */
	public ArduinoDataModel() {

	}

	// Getters and Setters

	/**
	 * @param toolbarVoltageListener the toolbarVoltageListener to set
	 */
	public void setToolbarVoltageListener(ToolbarVoltageListener toolbarVoltageListener) {
		this.toolbarVoltageListener = toolbarVoltageListener;
	}

	/**
	 * @param toolbarLabelTextListener the toolbarLabelTextListener to set
	 */
	public void setToolbarLabelTextListener(ToolbarLabelTextListener toolbarLabelTextListener) {
		this.toolbarLabelTextListener = toolbarLabelTextListener;
	}

	/**
	 * @param lineGraphListener the lineGraphListener to set
	 */
	public void setLineGraphListener(LineGraphListener lineGraphListener) {
		this.lineGraphListener = lineGraphListener;
	}

	/**
	 * @return the buttonText
	 */
	public String getButtonText() {
		return buttonText;
	}

	/**
	 * @param buttonText the buttonText to set
	 */
	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

	/**
	 * @return the choenPortName
	 */
	public String getChoenPortName() {
		return choenPortName;
	}

	/**
	 * @param choenPortName the choenPortName to set
	 */
	public void setChoenPortName(String choenPortName) {
		this.choenPortName = choenPortName;
	}

	// Methods

	/**
	 * Method will close Arduino Port if open when called.
	 */
	public void ensurePortClosed() {
		// try catch required if chosenPort is not open
		// will throw error as thread not running if it has not been started
		try {
			if (chosenPort.isOpen()) {
				chosenPort.closePort();
			}
		} catch (Exception e) {
			System.err.println("Thread not active to check if portOpen\nexpection handeled");
		}
	}

	/**
	 * Reads data from Arduino port Passes information to be read to change labels
	 * in the Toolbar GUI Class Passes information to be read to change lineGraph
	 * class
	 */
	public void readPortData() {
		
		// Check if connected to port
		if (buttonText.equalsIgnoreCase("Connect")) {
			// attempt to connect to the serial port
			// get the text of the serial port and use in the get commPort
			try {
				chosenPort = SerialPort.getCommPort(choenPortName);
			} catch (Exception exception) {
				// Exception should be handled
				System.err.println("Port not selected, displayed error to user");
				// Update toolbar labels with key numbers
				toolbarLabelTextListener.toolbarLabelSwitcher(1);

			}
			// read in a string from the scanner and leave port open
			chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);

			if (chosenPort.openPort()) {
				// Update toolbar labels with key number 2
				toolbarLabelTextListener.toolbarLabelSwitcher(2);
			} // end of if statement
				// Create a new thread that listens for incoming values from Arduino
			Thread thread = new Thread() {

				@Override
				public void run() {
					// using scanner read in the data from the Arduino/port data
					Scanner sc = new Scanner(chosenPort.getInputStream());

					// Loop through data while available
					while (sc.hasNextLine()) {

						// Try Catch required as Ardunio can send unclear data
						try {
							double numberOne = 0;
							double numberTwo = 0;

							// Data passed as a multiple of data in-take *3 (will be divided by 3 to receive
							// average)
							// This is used to smooth out data
							for (int loop = 0; loop < 3; loop++) {
								String line = sc.nextLine();
								// Split the csv by comma and store in double var
								String[] lineData = line.split(",");
								numberOne += Double.parseDouble(lineData[0]);
								numberTwo += Double.parseDouble(lineData[1]);
								//System.out.println(line);
								sc.nextLine();
							}
							//divide number by three to receive average
							numberOne = (numberOne/3);
							numberTwo = (numberTwo/3);
							
							// Process the data
							//Get Instance used to populate only one instance of Arduino Data Model class in ArduinoDataContentServer 
							ArduinoDataContentServer.getInstance().arduinoDataModel.processArduinoData(numberOne, numberTwo);
							ArduinoDataContentServer.getInstance().sendToConsumers(numberOne, numberTwo);

						} catch (Exception exception) { 
							System.err.println("ArduinoDataModel: Error in recieved Arduino Data");
						}
					} // end of while loop

					// Close scanner stop resource leak
					sc.close();

				}// end of run()

			};// end of thread

			// Start thread
			thread.start();
		} else if (buttonText.equalsIgnoreCase("Disconnect")) {
			// Disconnect from serial port
			chosenPort.closePort();
			// Update toolbar labels with key number 3
			toolbarLabelTextListener.toolbarLabelSwitcher(3);
		}
	}

	/**
	 * Process ArduinoData Convert analog reading (0 and 1023) to 0V to 5v (Arduino
	 * reads 0-5Vs) Data sent is a multiple of 3 reading and average is taken to
	 * smooth data reading (to make clearer to read and display on graph) Conversion
	 * is ((data reading /3) * (5.0 / 1023.0)) Runs Platform.runLater to reduce
	 * application UI freezing, Takes the data from the Arduino Thread and passes to
	 * the Application Thread
	 * 
	 * @param hasNextLine
	 */
	private void processArduinoData(Double numberOne, Double numberTwo) {

		try {
			
			//Process ArduinoData Convert analog reading (0 and 1023) to 0V to 5v
			double voltageOne = ((numberOne) * (5.0 / 1023.0));
			double voltageTwo = ((numberTwo) * (5.0 / 1023.0));
			
			//Adjust reading if above or below 0V or 5V
			//Arduino can only read 0V to 5V clean reading to error handle and smooth data further
			if (voltageOne > 5.0) {
				voltageOne = 5.0;
			} else if (voltageOne < 0.0) {
				voltageOne = 0.0;
			}

			if (voltageTwo > 5.0) {
				voltageTwo = 5.0;
			} else if (voltageTwo < 0.0) {
				voltageTwo = 0.0;
			}

			// Final required to pass to JavaFx Label
			final double passVoltOne = voltageOne;
			final double passVoltTwo = voltageTwo;

			// Convert double to decimalFormat of 2 places and display on label.
			DecimalFormat decimalFormat = new DecimalFormat("#.00");
			String v1Formatted = decimalFormat.format(passVoltOne);
			String v2Formatted = decimalFormat.format(passVoltTwo);
			//update interface method with Arduino reading to display in live label readings
			toolbarVoltageListener.toolbarVoltageLabelUpdate(v1Formatted, v2Formatted);

			//System.out.printf("Sensor One : %.2f, Sensor Two: %.2f\n", voltageOne, voltageTwo);

			//Update interface method with new Arduino reading to display in Graph
			lineGraphListener.dataProduced(new XYChart.Data<Number, Number>(sensorOneXAxisCounter++, passVoltOne),
					new XYChart.Data<Number, Number>(sensorTwoXAxisCounter++, passVoltTwo));

		} catch (Exception exception) {

		}

	}

}
