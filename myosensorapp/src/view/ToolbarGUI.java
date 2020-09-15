/**
 * 
 */
package view;

import controller.ChosenPortListener;
import controller.OpenKeyControlGUIListener;
import controller.RecordDataListener;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * @author johnmcculloch Toolbar GUI Class sets up components to create the
 *         Toolbar GUI
 */
public class ToolbarGUI extends HBox {

	// Instance vars

	/**
	 * VBox to store PortNames ComboBox and PortName Checkbox
	 */
	private VBox vbox;

	/**
	 * Stores list of computer Ports USB Port to Link Arduino To Program
	 */
	private ComboBox<String> portList;

	/**
	 * Once Port Selected button to connect
	 */
	private Button connectButton;

	/**
	 * Once selected will open key controller GUI
	 */
	private Button keyControl;

	/**
	 * Once selected will start recording of data Allows values to be displayed
	 * until user happy with placement of electrodes
	 */
	private Button recordDataButton;

	/**
	 * Voltage Label
	 */
	private Label voltageLabel;

	/**
	 * Displays MyoSensor Reading
	 */
	private Label displayReadingOne;

	/**
	 * Displays MyoSensor Reading
	 */
	private Label displayReadingTwo;

	/**
	 * Check Box Store Port Name
	 */
	private CheckBox checkBoxPortName;

	/**
	 * Chosen PortListener (Activates selected port to open and read data)
	 */
	private ChosenPortListener chosenPortListener;

	/**
	 * Listen to when KeyControl button is selected (activate KeyControlGUI in
	 * Controller)
	 */
	private OpenKeyControlGUIListener openKeyControlGUIListener;

	/**
	 * Listen to when Record Data button is selected
	 */
	private RecordDataListener recordDataListener;

	/**
	 * Font
	 */
	private Font font;

	/**
	 * Colour of font
	 */
	private Color c1, c2;

	// Constructors

	/**
	 * Default Constructor
	 */
	public ToolbarGUI() {

		// Font to be used for GUI
		font = new Font("Arial", 20);

		// Colours To Style Label
		c1 = Color.rgb(244, 105, 54);
		c2 = Color.rgb(251, 167, 27);

		// set default settings of HBox
		setPadding(new Insets(10, 10, 10, 10));
		setSpacing(10);

		// Initialise instance of labels
		voltageLabel = new Label();
		voltageLabel.setFont(font);

		displayReadingOne = new Label();
		displayReadingOne.setFont(font);
		displayReadingOne.setTextFill(c1);

		displayReadingTwo = new Label();
		displayReadingTwo.setFont(font);
		displayReadingTwo.setTextFill(c2);

		// Set visible to false (only to show when connectButton active
		voltageLabel.setVisible(false);
		displayReadingOne.setVisible(false);
		displayReadingTwo.setVisible(false);

		// create ComboBox
		portList = new ComboBox<String>();
		portList.setPromptText("Select Port");

		// Store PortName
		checkBoxPortName = new CheckBox("Store Port");
		checkBoxPortName.setPadding(new Insets(5, 5, 5, 5));

		// Vbox store PortNames and PortSave Checkbox
		vbox = new VBox();
		vbox.getChildren().addAll(portList, checkBoxPortName);

		// create ConnectButton
		connectButton = new Button("Connect");
		connectButton.setOnAction(e -> {
			// run method to perform actions required following connect button clicked
			connectButtonAction();
		});

		// Create record data button
		recordDataButton = new Button("Record Data");
		recordDataButton.setVisible(false);
		recordDataButton.setOnAction(e -> {
			recordDataButtonAction();
		});

		// key controller button (to open key controller GUI)
		keyControl = new Button("Key Controller");
		keyControl.setAlignment(Pos.TOP_RIGHT);
		keyControl.setOnAction(e -> {
			openKeyControlGUIListener.openKeyControlGUI();
		});

		// add items to hBox
		getChildren().addAll(vbox, connectButton, recordDataButton, voltageLabel, displayReadingOne, displayReadingTwo,
				keyControl);

	}

	// Getters and Setters

	/**
	 * @param chosenPortListener the chosenPortListener to set
	 */
	public void setChosenPortListener(ChosenPortListener chosenPortListener) {
		this.chosenPortListener = chosenPortListener;
	}

	/**
	 * @param openKeyControlGUIListener the openKeyControlGUIListener to set
	 */
	public void setOpenKeyControlGUIListener(OpenKeyControlGUIListener openKeyControlGUIListener) {
		this.openKeyControlGUIListener = openKeyControlGUIListener;
	}

	/**
	 * @param recordDataListener the recordDataListener to set
	 */
	public void setRecordDataListener(RecordDataListener recordDataListener) {
		this.recordDataListener = recordDataListener;
	}

	// Methods

	/**
	 * Connect Button Action This method performs the actions following the connect
	 * button clicked
	 */
	private void connectButtonAction() {

		// check if port is null if so add error message
		try {
			chosenPortListener.chosenPortName(portList.getSelectionModel().getSelectedItem().toString(),
					connectButton.getText(), checkBoxPortName.isSelected());
		} catch (NullPointerException exception) {
			System.err.println("Port not selected, displayed error to user");
			// update labels using key one to inform user to select a port
			updateToolBarLabelText(1);
		}

	}

	/**
	 * Record Data Button Action This method performs the actions following the
	 * record data button clicked
	 */
	private void recordDataButtonAction() {

		try {
			// Update in main controller button name
			recordDataListener.recordData(recordDataButton.getText());

			Platform.runLater(() -> {

				// Update Button Text
				if (recordDataButton.getText().equalsIgnoreCase("Record Data")) {
					recordDataButton.setText("Stop Data\nRecording");
					recordDataButton.setStyle("-fx-background-color:#ff2929;");
				} else {
					recordDataButton.setText("Record Data");
					recordDataButton.setStyle(null);
				}

			});

		} catch (Exception e) {
			System.err.println("Error passing record data button name");
		}

	}

	/**
	 * Populates portList combobox GUI
	 * 
	 * @param portListed ObservableList<String>
	 */
	public void populatePortList(ObservableList<String> portListed) {

		Platform.runLater(() -> {
		portList.setItems(portListed);
		});

	}

	/**
	 * Set Selected PortName
	 * 
	 * @param portName
	 */
	public void setSavedPortName(String portName) {

		// if portName is null no file name found.
		if (portName != null) {
			try {
				// check if the port still exists if so select that port
				// if not leave default set-up
				for (String list : this.portList.getItems()) {
					if (list.equalsIgnoreCase(portName)) {
						// if true set port to selected portName
						portList.getSelectionModel().select(portName);
						// set check box to checked / ticked
						checkBoxPortName.setSelected(true);
					}
				}

			} catch (Exception e) {
			}
		}

	}

	/**
	 * Update labels with live reading from Arduino data Labels used as a visual
	 * guide to user in GUI
	 * 
	 * @param voltageOne
	 * @param voltaeTwo
	 */
	public void updateToolBarLabels(String voltageOne, String voltaeTwo) {
		Platform.runLater(() -> displayReadingOne.setText(voltageOne));
		Platform.runLater(() -> displayReadingTwo.setText(voltaeTwo));
	}

	public void updateToolBarLabelText(int num) {
		switch (num) {
		case 1:
			Platform.runLater(() -> {
				voltageLabel.setVisible(true);
				voltageLabel.setText("Please choose a port");
			});
			break;
		case 2:
			Platform.runLater(() -> {
				// Set text of button to disconnect
				connectButton.setText("Disconnect");
				connectButton.setStyle("-fx-background-color:#ff2929;");
				// Set labels to true (so they are visable)
				recordDataButton.setVisible(true);
				voltageLabel.setVisible(true);
				voltageLabel.setText("Voltage: ");
				displayReadingOne.setVisible(true);
				displayReadingTwo.setVisible(true);
				// Disable PortList (So you can not switch while reading from Port)
				portList.setDisable(true);
				checkBoxPortName.setDisable(true);
			});
			break;
		case 3:
			Platform.runLater(() -> {
				// Show port List
				portList.setDisable(false);
				checkBoxPortName.setDisable(false);
				// Hide voltage labels and readings (as not active)
				voltageLabel.setVisible(false);
				displayReadingOne.setVisible(false);
				displayReadingTwo.setVisible(false);
				recordDataButton.setVisible(false);
				connectButton.setText("Connect");
				connectButton.setStyle(null);
			});
			//Update record Data Button Action
			//If user disconnects from data stop recording data, run method
			if(recordDataButton.getText().equalsIgnoreCase("Stop Data\nRecording")) {
				recordDataButtonAction();
			}
			break;
		default:
		}

	}

}// end of Toolbar class
