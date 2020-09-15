/**
 * 
 */
package controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.ArduinoDataContentServer;
import model.KeyboardControlModel;
import model.PortListModel;
import model.UserDetails;
import model.WriteToFile;
import view.KeyboardControlGUI;
import view.LineGraphGUI;
import view.ProgramCloseConfirmationGUI;
import view.ResetGUI;
import view.ToolbarGUI;
import view.UserDetailsGUI;
import view.UserDetailsGUITitle;
import view.WelcomeGUI;

/**
 * @author johnmcculloch MainCnotroller Class Connects Model (Data Class's) with
 *         View (GUI JavaFx Class's)
 */
public class MainController extends Application {

	// Instance Vars
	/**
	 * Primary window to set the scene
	 */
	private Stage window;

	/**
	 * Tool bar in layout HBox
	 */
	private ToolbarGUI toolbar;

	/**
	 * Welcome GUI
	 */
	private WelcomeGUI welcomeGUI;

	/**
	 * Reset GUI
	 */
	private ResetGUI resetGUI, resetGUITwo;

	/**
	 * Line Graph
	 */
	private LineGraphGUI lineGraph;

	/**
	 * User Details GUI
	 */
	private UserDetailsGUI userDetailsGUI;

	/**
	 * User Details GUI Title
	 */
	private UserDetailsGUITitle userDetailsGUITitle;

	/**
	 * User Details
	 */
	private UserDetails userDetails;

	/**
	 * Write To File
	 */
	private WriteToFile writeToFile;

	/**
	 * Graph Scene, User Details GUI Scene
	 */
	private Scene welcomeScene, graphScene, userDetailsScene;

	/**
	 * Layout management for Graph Scene and UserDetails Scene
	 */
	private BorderPane graphBorderPane, userDetailsBorderPane;

	/**
	 * Program Close Confirmation
	 */
	private ProgramCloseConfirmationGUI closeConfirmation;

	/**
	 * Keyboard Control GUI
	 */
	private KeyboardControlGUI keyboardControlGUI;

	/**
	 * Keyboard Control Model
	 */
	private KeyboardControlModel keyboardControlModel;

	/**
	 * Port List Model
	 */
	private PortListModel portListModel;
	
	/**
	 * Boolean used to flag recording of data to file
	 * True = record data
	 * False = do not record data
	 */
	private Boolean recordData = false;

	// Methods

	/**
	 * Set Primary Stage Controller between Model and View
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		// Instantiate Objects

		welcomeGUI = new WelcomeGUI();

		resetGUI = new ResetGUI();

		resetGUITwo = new ResetGUI();

		closeConfirmation = new ProgramCloseConfirmationGUI();

		keyboardControlGUI = new KeyboardControlGUI();

		keyboardControlModel = new KeyboardControlModel();

		portListModel = new PortListModel();

		toolbar = new ToolbarGUI();

		userDetailsGUITitle = new UserDetailsGUITitle();

		userDetailsGUI = new UserDetailsGUI();

		lineGraph = new LineGraphGUI();

		writeToFile = new WriteToFile();

		// Communication between Models and Views

		// Listen to change between welcome page and user details
		welcomeGUI.setChangeSceneListener(new ChangeSceneListener() {

			@Override
			public void changeScene() {
				// window.setMinHeight(600);
				// window.setMinWidth(800);
				window.setWidth(800);
				window.setHeight(600);
				window.setScene(userDetailsScene);

			}
		});

		// check User Details GUI Patient ID before displaying button to proceed to
		// Graph GUI
		userDetailsGUI.setPatientIDDetailsListener(new PatientIDDetailsListener() {

			@Override
			public Boolean checkUserDetails(String paitentID) {

				return checkPatientID(paitentID);

			}
		});

		// check User Details GUI Notes before displaying button to proceed to Graph GUI
		userDetailsGUI.setNotesDetailsListener(new NotesDetailsListener() {

			@Override
			public Boolean checkNotesInUserDetails(String notes) {

				return checkNotesBuinessRules(notes);
			}
		});

		// Activate scene change between userPersonal Details and GraphView
		// Write to file
		userDetailsGUI.setChangeDataSceneListener(new ChangeSceneDataListener() {

			@Override
			public void changeScene(String patientID, String notes, String amputationSide) {

				try {
					// Set the User Details and FileName to which the method will write to file
					writeToFile.setUserDetails(userDetailsToWriteToFile(patientID, notes, amputationSide));
					writeToFile.setFileToWriteTo(createFileName(patientID));

				} catch (Exception e) {
					System.err.println("Opps something went wonrg writing to file");
				}

				try {
					//reload portlist
					portListModel.populatePortNamesList();
					// Populate portList between toolbar and portListModel
					toolbar.populatePortList(portListModel.getPortNamesObservableList());
					// Check if there is a saved portName
					toolbar.setSavedPortName(portListModel.deserializePortName());
				} catch (Exception e) {
					System.err.println("Opps something went wrong with check populating PortListNames");
				}

				window.setHeight(600);
				window.setWidth(800);
				window.setScene(graphScene);

			}
		});

		// open KeyControl GUI when selected in toolbar
		toolbar.setOpenKeyControlGUIListener(new OpenKeyControlGUIListener() {

			@Override
			public void openKeyControlGUI() {
				// open keyboard GUI
				keyboardControlGUI.show();
			}
		});
		
		//Trigger recording of data
		toolbar.setRecordDataListener(new RecordDataListener() {
			
			@Override
			public void recordData(String recordDataButtonName) {
				
				//if user selected to record data button text "Record Data" used to indicate
				//changed following trigger to "Stop Recording Data"
				//Button text used to change boolean of record Data
				if(recordDataButtonName.equalsIgnoreCase("Record Data")) {
					recordData = true;
				} else {
					recordData = false;
				}
				
			}
		});

		// Connect Keyboard Control GUI Slider to Model pin A0 (Signal A0)
		keyboardControlGUI.setSliderKeyValueListenerA0(new SliderKeyValueListenerA0() {

			@Override
			public void sliderKeyValuesA0(double a0SliderValue) {
				keyboardControlModel.setA0SliderThresholdValue(a0SliderValue);

			}
		});

		// Connect Keyboard Control GUI Slider to Model pin A1 (Signal A1)
		keyboardControlGUI.setSliderKeyValueListenerA1(new SliderKeyValueListenerA1() {

			@Override
			public void sliderKeyValuesA1(double a1SliderValue) {
				keyboardControlModel.setA1SliderThresholdValue(a1SliderValue);

			}
		});

		// set keys selected in GUI to model to process
		keyboardControlGUI.setKeySelectionListener(new KeySelectionListener() {

			@Override
			public void setKeySelection(KeyCode a0Key, KeyCode a1Key, KeyCode a01Key, boolean mouseControlXAxisSelected,
					boolean mouseControlYAxisSelected, boolean mouseControlXAndYAxisSelected, String button) {

				try {
					// depends on if connect button is connected or not
					if (button.equalsIgnoreCase("Connect")) {
						// set keys or mouse controls selected
						keyboardControlModel.setA0Key(a0Key);
						keyboardControlModel.setA1Key(a1Key);
						keyboardControlModel.setA01Key(a01Key);
						keyboardControlModel.setMouseControlXAxisSelected(mouseControlXAxisSelected);
						keyboardControlModel.setMouseControlYAxisSelected(mouseControlYAxisSelected);
						keyboardControlModel.setMouseControlXAndYAxisSelected(mouseControlXAndYAxisSelected);
						// set running to true
						keyboardControlModel.start();
					} else {
						// if button is dissconenct stop the thread
						keyboardControlModel.stop();
					}
				} catch (Exception e) {
					System.err.println("Main Controller Keyboard Control Error Handeled");
				}

			}
		});

		// Check usser want to close keyboard control GUI when x selected
		keyboardControlGUI.setOnCloseRequest(e -> {
			// stop request (consume) method will ask question and
			// process closing of window
			e.consume();
			if (closeConfirmation.checkUserWantsToCloseProgram("Are you sure you want to close the window?")) {
				// check if thread is still alive if so stop
				if (keyboardControlModel.isRunning()) {
					// if running stop thread
					keyboardControlModel.stop();
				}
				// close current open window
				keyboardControlGUI.close();
			}
		});

		// Populate portList between toolbar and portListModel
		toolbar.populatePortList(portListModel.getPortNamesObservableList());
		// Check if there is a saved portName
		toolbar.setSavedPortName(portListModel.deserializePortName());

		// Set Tool Bar Text Setup Depending on state of arduino
		ArduinoDataContentServer.getInstance().arduinoDataModel
				.setToolbarLabelTextListener(new ToolbarLabelTextListener() {

					@Override
					public void toolbarLabelSwitcher(int num) {
						toolbar.updateToolBarLabelText(num);

					}
				});

		// Send selected port and connect button to Arduino
		toolbar.setChosenPortListener(new ChosenPortListener() {

			@Override
			public void chosenPortName(String portName, String buttonText, boolean savePortName) {

				// Check if user wants to store portName (Serialize Save to Memory)
				if (savePortName) {
					portListModel.serializePortName(portName);
				}

				ArduinoDataContentServer.getInstance().arduinoDataModel.setChoenPortName(portName);
				ArduinoDataContentServer.getInstance().arduinoDataModel.setButtonText(buttonText);
				ArduinoDataContentServer.getInstance().arduinoDataModel.readPortData();

			}
		});

		// Update Tool Bar with pin A0 and A1 Signals and display
		ArduinoDataContentServer.getInstance().arduinoDataModel.setToolbarVoltageListener(new ToolbarVoltageListener() {

			@Override
			public void toolbarVoltageLabelUpdate(String voltage1, String voltage2) {
				toolbar.updateToolBarLabels(voltage1, voltage2);
			}
		});

		// Listen for when data has been received and set through the listener
		// Update Line Graph display and write to file the recordings
		ArduinoDataContentServer.getInstance().arduinoDataModel.setLineGraphListener(new LineGraphListener() {

			@Override
			public void dataProduced(Data<Number, Number> sensorOne, Data<Number, Number> sensorTwo) {
				lineGraph.addSeriesData(sensorOne, sensorTwo);
				
				//Boolean to trigger record data, activated on true condition
				//Triggered by user on graph GUI
				if(recordData) {
				writeToFile.collectArduinoDataForFile(sensorOne, sensorTwo);
				}

			}
		});

		// Primary Stage Layout

		// Primary Stage
		window = primaryStage;
		window.setTitle("MyoSensor Reading");
		// set on close request (check they want to close)
		// using lambda expression and event (e) to action method
		window.setOnCloseRequest(e -> {
			// stop request (consume) method will ask question and
			// process closing of window
			e.consume();
			if (closeConfirmation.checkUserWantsToCloseProgram("Are you sure you want to exit the program?")) {
				
				// if true ensure port is closed
				// arduinoDataModel.ensurePortClosed();
				ArduinoDataContentServer.getInstance().arduinoDataModel.ensurePortClosed();

				// Check if Keyboard GUI is open, if so close along side main control
				if (keyboardControlGUI.isShowing()) {
					if (keyboardControlModel.isRunning()) {
						// if running stop thread
						keyboardControlModel.stop();
					}
					// close current open window
					keyboardControlGUI.close();
				}
				// write data to file (perform following closure of window and instance stopped from ArudinoModel)
				writeToFile.writeDetailsToFile();
				// close current open window
				window.close();
			}

		});

		resetGUI.setResetSceneListener(new ResetSceneListener() {
			// reset when triggered
			@Override
			public void resetScene() {
				try {
					// if true ensure port is closed
					// arduinoDataModel.ensurePortClosed();
					ArduinoDataContentServer.getInstance().arduinoDataModel.ensurePortClosed();

					// Check if Keyboard GUI is open, if so close along side main control
					if (keyboardControlGUI.isShowing()) {
						if (keyboardControlModel.isRunning()) {
							// if running stop thread
							keyboardControlModel.stop();
						}
						// close current open window
						keyboardControlGUI.close(); 
					}
					start(primaryStage);
				} catch (Exception e) {
				}

			}
		});

		resetGUITwo.setResetSceneListener(new ResetSceneListener() {
			// reset when triggered
			@Override
			public void resetScene() {
				try {
					// if true ensure port is closed
					// arduinoDataModel.ensurePortClosed();
					ArduinoDataContentServer.getInstance().arduinoDataModel.ensurePortClosed();

					// Check if Keyboard GUI is open, if so close along side main control
					if (keyboardControlGUI.isShowing()) {
						if (keyboardControlModel.isRunning()) {
							// if running stop thread
							keyboardControlModel.stop();
						}
						// close current open window
						keyboardControlGUI.close();
					}
					start(primaryStage);
				} catch (Exception e) {
				}

			}
		});
		
		// reset keyboard GUI keys
		keyboardControlGUI.setResetSceneListener(new ResetSceneListener() {
			
			@Override
			public void resetScene() {
				try {
					// Check if Keyboard GUI is open, if so close along side main control
					if (keyboardControlGUI.isShowing()) {
						if (keyboardControlModel.isRunning()) {
							// if running stop thread
							keyboardControlModel.stop();
						}
						// call reset key method in keyboard GUI
						keyboardControlGUI.resetKeyButtons();
					}
					
				} catch (Exception e) {
				}
				
			}
		});

		// Add different views with primary stage

		// Layout (Welcome GUI)

		// Layout (Graph Scene)
		graphBorderPane = new BorderPane();
		graphBorderPane.setPadding(new Insets(10, 25, 10, 10));
		graphBorderPane.setTop(toolbar);
		graphBorderPane.setCenter(lineGraph);
		graphBorderPane.setBottom(resetGUITwo);

		// Layout (User Details GUI)
		userDetailsBorderPane = new BorderPane();
		userDetailsBorderPane.setPadding(new Insets(20, 20, 20, 20));
		userDetailsBorderPane.setTop(userDetailsGUITitle);
		userDetailsBorderPane.setCenter(userDetailsGUI);
		userDetailsBorderPane.setBottom(resetGUI);

		// Set up Scenes
		welcomeScene = new Scene(welcomeGUI);
		userDetailsScene = new Scene(userDetailsBorderPane);
		graphScene = new Scene(graphBorderPane);

		// Set up window
		window.setScene(welcomeScene);
		window.setMinWidth(800);
		window.setMinHeight(600);
		window.setMaxWidth(950);
		window.setMaxHeight(750);
		window.setHeight(700);
		window.setWidth(900);
		window.show();

	}

	/**
	 * Lunch application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Try Catch in case of issue loading, allow program to re-load
		try {

			launch(args);

		} catch (Exception e) {
		}
	}

	/**
	 * Check patientID against UserDetails BuinessRules
	 * 
	 * @param patientID
	 * @return
	 */
	private boolean checkPatientID(String patientID) {
		try {
			userDetails = new UserDetails();
			userDetails.setPatientID(patientID);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Check patientID against UserDetails BuinessRules
	 * 
	 * @param patientID
	 * @return
	 */
	private boolean checkNotesBuinessRules(String notes) {
		try {
			userDetails = new UserDetails();
			userDetails.setNotes(notes);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Method takes PatientID , Notes , AmputationSide and returns formatted user
	 * details Method used to write user details formatted to file
	 * 
	 * @param patientID      String
	 * @param notes          String
	 * @param amputationSide String
	 * @return String
	 */
	private UserDetails userDetailsToWriteToFile(String patientID, String notes, String amputationSide) {

		try {
			// Pass user information into an instance of userDetails
			// Return print User Details method to print (formatted details) to file
			userDetails.setPatientID(patientID);
			userDetails.setNotes(notes);
			userDetails.setAmputationSide(amputationSide);
			userDetails.setDateTime(new Date().toString());

			return userDetails;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Created File Name PatientID and Current Date Stamp
	 * 
	 * @param patientID
	 * @return String fileName
	 */
	private String createFileName(String patientID) {
		// Create file name consisting of patientID and Current Date/Time Stamp
		String fileName = patientID;

		Date date = new Date();
		// convert date to local date to get month year and day to make file name
		// date .getDay() ect is depreciated after java8
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		int year = localDate.getYear();
		int month = localDate.getMonthValue();
		int day = localDate.getDayOfMonth();
		// add date to patient id and .json file extension
		fileName += "_" + day + "_" + month + "_" + year;

		return fileName;

	}

}
