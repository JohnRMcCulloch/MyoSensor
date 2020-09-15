/**
 * 
 */
package view;

import java.text.NumberFormat;

import controller.KeySelectionListener;
import controller.ResetSceneListener;
import controller.SliderKeyValueListenerA0;
import controller.SliderKeyValueListenerA1;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * @author johnmcculloch Keyboard Control GUI Class sets up components to create
 *         the keyboard GUI
 */
public class KeyboardControlGUI extends Stage {

	// Instance Variables
	/**
	 * Scene
	 */
	private Scene scene;

	/**
	 * Grid Pane
	 */
	private GridPane gridPane;

	/**
	 * Labels
	 */
	private Label sliderA0Label, sliderA1Label, titleLabel, a0Label, a1Label, a0A1Label, a0SliderValue, a1SliderValue,
			mouseControlLabel;

	/**
	 * Sensitivity Slider
	 */
	private Slider a0Slider, a1Slider;

	/**
	 * Button
	 */
	private Button a0Button, a1Button, a0A1Button, connectButton, resetButton, mouseButtonXAxis, mouseButtonYAxis,
			mouseButtonXAndYAxis;

	/**
	 * Font
	 */
	private Font standardFont, titleFont;

	/**
	 * VBox layout
	 */
	private VBox sliderA0, sliderA1, mouseVBox;

	/**
	 * HBox layout
	 */
	private HBox keyMapInputA0, keyMapInputA1, keyMapInputA0A1;

	/**
	 * Flag used to trigger key listen
	 */
	private static boolean flagA0 = false;

	/**
	 * Flag used to trigger key listen
	 */
	private static boolean flagA1 = false;

	/**
	 * Flag used to trigger key listen
	 */
	private static boolean flagA0A1 = false;

	/**
	 * Flag to trigger Connect button to disable
	 */
	private static boolean flagConnectDisable = false;

	/**
	 * Values to pass to keyController.
	 */
	private KeyCode a0Key, a1Key, a01Key;

	/**
	 * Key Selection Listener;
	 */
	private KeySelectionListener keySelectionListener;

	/**
	 * Slider Key Threshold A0 Signal;
	 */
	private SliderKeyValueListenerA0 sliderKeyValueListenerA0;

	/**
	 * Slider Key Threshold A1 Signal;
	 */
	private SliderKeyValueListenerA1 sliderKeyValueListenerA1;
	
	/**
	 * Reset Scene Listener
	 */
	private ResetSceneListener resetSceneListener;

	/**
	 * Mouse Control X Axis Selected Yes - Selected No - Not Selected
	 */
	private boolean mouseControlXAxisSelected = false;

	/**
	 * Mouse Control Y Axis Selected Yes - Selected No - Not Selected
	 */
	private boolean mouseControlYAxisSelected = false;

	/**
	 * Mouse Control X and Y Axis Selected Yes - Selected No - Not Selected
	 */
	private boolean mouseControlXAndYAxisSelected = false;

	// Constructors

	/**
	 * Default Constructor
	 */
	public KeyboardControlGUI() {

		// Initialise the key as null
		a0Key = null;
		a1Key = null;
		a01Key = null;

		// Font
		standardFont = new Font("Arial", 15);
		titleFont = new Font("Arial", 25);

		// Title
		titleLabel = new Label("Keyboard Controller");
		titleLabel.setFont(titleFont);
		GridPane.setConstraints(titleLabel, 0, 0);

		// Sliders (inside an VBox)
		sliderA0Label = new Label("Key Trigger\nA0 Threshold (0-5V)");
		sliderA0Label.setFont(standardFont);

		sliderA1Label = new Label("Key Trigger\nA1 Threshold (0-5V)");
		sliderA1Label.setFont(standardFont);

		a0SliderValue = new Label();
		a0SliderValue.setFont(standardFont);

		a1SliderValue = new Label();
		a1SliderValue.setFont(standardFont);

		// 0 - low value, 5 - high value, 2.5 initial value
		a0Slider = new Slider(0, 5, 2.5);
		a0Slider.setMinWidth(200);
		a1Slider = new Slider(0, 5, 2.5);
		a1Slider.setMinWidth(200);

		// Update label with incoming value from slider (0-5V) (Arduino set at 0-5V);
		a0SliderValue.textProperty().bindBidirectional(a0Slider.valueProperty(), NumberFormat.getInstance());
		a1SliderValue.textProperty().bindBidirectional(a1Slider.valueProperty(), NumberFormat.getInstance());

		// Listen to changes in the slider and pass new value along to listener
		a0Slider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
				sliderKeyValueListenerA0.sliderKeyValuesA0(newValue.doubleValue());
			}

		});

		// Listen to changes in the slider and pass new value along to listener
		a1Slider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
				sliderKeyValueListenerA1.sliderKeyValuesA1(newValue.doubleValue());
			}

		});

		sliderA0 = new VBox();
		sliderA0.setSpacing(10);
		// sliderA0.setPadding(new Insets(10, 10, 10, 10));
		sliderA0.getChildren().addAll(sliderA0Label, a0SliderValue, a0Slider);
		GridPane.setConstraints(sliderA0, 0, 1);

		sliderA1 = new VBox();
		sliderA1.setSpacing(10);
		// sliderA1.setPadding(new Insets(10, 10, 10, 10));
		sliderA1.getChildren().addAll(sliderA1Label, a1SliderValue, a1Slider);
		GridPane.setConstraints(sliderA1, 1, 1);

		// A0 and A1 Key input
		a0Label = new Label("A0:");
		a0Label.setFont(standardFont);

		a1Label = new Label("A1:");
		a1Label.setFont(standardFont);

		a0A1Label = new Label("A0+1:");
		a0A1Label.setFont(standardFont);

		a0Button = new Button("Set Key");
		a1Button = new Button("Set Key");
		a0A1Button = new Button("Set Key");

		a0Button.setOnAction(e -> {
			// If the key is set to Set Key
			// Change to Type Key (Prompt user to type)
			if (a0Button.getText().equals("Set Key")) {
				a0Button.setText("Type Key");
				flagA0 = true;
			}

		});

		a1Button.setOnAction(e -> {
			// If the key is set to Set Key
			// Change to Type Key (Prompt user to type)
			if (a1Button.getText().equals("Set Key")) {
				a1Button.setText("Type Key");
				flagA1 = true;
			}
		});

		a0A1Button.setOnAction(e -> {
			// If the key is set to Set Key
			// Change to Type Key (Prompt user to type)
			if (a0A1Button.getText().equals("Set Key")) {
				a0A1Button.setText("Type Key");
				flagA0A1 = true;
			}
		});

		keyMapInputA0 = new HBox();
		keyMapInputA0.setPadding(new Insets(10, 10, 10, 10));
		keyMapInputA0.setSpacing(20);
		keyMapInputA0.getChildren().addAll(a0Label, a0Button);
		GridPane.setConstraints(keyMapInputA0, 0, 2);

		keyMapInputA1 = new HBox();
		keyMapInputA1.setPadding(new Insets(10, 10, 10, 10));
		keyMapInputA1.setSpacing(20);
		keyMapInputA1.getChildren().addAll(a1Label, a1Button);
		GridPane.setConstraints(keyMapInputA1, 1, 2);

		keyMapInputA0A1 = new HBox();
		keyMapInputA0A1.setPadding(new Insets(10, 10, 10, 10));
		keyMapInputA0A1.setSpacing(20);
		keyMapInputA0A1.getChildren().addAll(a0A1Label, a0A1Button);
		GridPane.setConstraints(keyMapInputA0A1, 0, 3);
		// keyMapInputA0A1.setVisible(false);

		//Connect Button 
		connectButton = new Button("Connect");
		connectButton.setFont(standardFont);
		connectButton.setMinSize(80, 50);
		GridPane.setConstraints(connectButton, 1, 4);
		
		resetButton = new Button("Reset Keys");
		GridPane.setConstraints(resetButton, 1, 3);
		
		// Mouse Control
		mouseControlLabel = new Label("Mouse Control : ");
		mouseControlLabel.setFont(standardFont);
		mouseButtonXAxis = new Button("Mouse X Axis + Left Click");
		mouseButtonYAxis = new Button("Mouse Y Axis + Left Click");
		mouseButtonXAndYAxis = new Button("Mouse X & Y");
		mouseVBox = new VBox();
		mouseVBox.setSpacing(10);
		mouseVBox.getChildren().addAll(mouseControlLabel, mouseButtonXAxis, mouseButtonYAxis, mouseButtonXAndYAxis);
		GridPane.setConstraints(mouseVBox, 0, 4);

		// If Mouse Button X Axis selected, disable other options
		// Set boolean to true;
		mouseButtonXAxis.setOnAction(e -> {
			mouseButtonXAxisAction();
		});

		// If Mouse Button Y Axis selected, disable other options
		// Set boolean to true;
		mouseButtonYAxis.setOnAction(e -> {
			mouseButtonYAxisActoin();
		});

		// If Mouse Button X And Y Axis selected, disable other options
		// Set boolean to true;
		mouseButtonXAndYAxis.setOnAction(e -> {
			mouseButtonXAndYAxisAction();
		});

		// Connect Button Disable buttons is selected
		connectButton.setOnAction(e -> {
			connectButtonAction();
		});
		
		resetButton.setOnAction(e ->{
			Platform.runLater(() -> {
				resetSceneListener.resetScene();
			});
		});
		
		// GridPane SetUp
		gridPane = new GridPane();
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		gridPane.setVgap(30);
		gridPane.setHgap(10);
		gridPane.setAlignment(Pos.CENTER);
		gridPane.getChildren().addAll(titleLabel, sliderA0, sliderA1, keyMapInputA0, keyMapInputA1, keyMapInputA0A1,
				mouseVBox, connectButton, resetButton);

		// Instantiate stage and set scene
		setTitle("Key Controller");
		scene = new Scene(gridPane, 700, 600);
		setMinHeight(550);
		setMinWidth(650);
		setScene(scene);
		setAlwaysOnTop(true);

		// add event filter
		// listens to key events , to flags handle the request to process the key press
		addEventFilter(KeyEvent.KEY_PRESSED, e -> {

			keyFunctions(e);
			// consume the function of the key (gets ready to listen again)
			e.consume();
		});
	}

	// Getters and Setters

	/**
	 * @param keySelectionListener the keySelectionListener to set
	 */
	public void setKeySelectionListener(KeySelectionListener keySelectionListener) {
		this.keySelectionListener = keySelectionListener;
	}

	/**
	 * @param sliderKeyValueListener the sliderKeyValueListener to set
	 */
	public void setSliderKeyValueListenerA0(SliderKeyValueListenerA0 sliderKeyValueListenerA0) {
		this.sliderKeyValueListenerA0 = sliderKeyValueListenerA0;
	}

	/**
	 * @param sliderKeyValueListenerA1 the sliderKeyValueListenerA1 to set
	 */
	public void setSliderKeyValueListenerA1(SliderKeyValueListenerA1 sliderKeyValueListenerA1) {
		this.sliderKeyValueListenerA1 = sliderKeyValueListenerA1;
	}
	
	/**
	 * @param resetSceneListener the resetSceneListener to set
	 */
	public void setResetSceneListener(ResetSceneListener resetSceneListener) {
		this.resetSceneListener = resetSceneListener;
	}

	// Methods
	/**
	 * Key Event / Functions This method handles the key functions to be set and
	 * passed to controller
	 * 
	 * @param e KeyEvent
	 */
	private void keyFunctions(KeyEvent e) {

		// Flag is set true is X&Y Control is selected
		if (flagConnectDisable) {
			// if escape key is pressed fire connectButton (disable button)
			if (e.getCode() == KeyCode.ENTER) {
				connectButton.fire();
			}
		}

		if (flagA0) {

			a0Label.setText(e.getCode().toString());
			// set the key code to be passed
			a0Key = e.getCode();
			flagA0 = false;
			a0Button.setText("Set Key");
			// System.out.println("A0: " + e.getCode().toString());
		}

		if (flagA1) {

			a1Label.setText(e.getCode().toString());
			// set the key code to be passed
			a1Key = e.getCode();
			flagA1 = false;
			a1Button.setText("Set Key");
			// System.out.println("A1: " + e.getCode().toString());
		}

		if (flagA0A1) {

			a0A1Label.setText(e.getCode().toString());
			// set the key code to be passed
			a01Key = e.getCode();
			flagA0A1 = false;
			a0A1Button.setText("Set Key");
			// System.out.println("Co-Contraction: " + e.getCode().toString());
		}

	}

	/**
	 * Mouse X Axis Action This method runs the action to be performed follow mouse
	 * x button clicked
	 */
	private void mouseButtonXAxisAction() {
		// clicked so change to true if false
		// If not - true (Flag as true)
		if (!mouseControlXAxisSelected) {
			mouseControlXAxisSelected = true;
			Platform.runLater(() -> {
				mouseButtonXAxis.setText("SELECTED :Mouse X Axis + Left Click");

				mouseButtonYAxis.setDisable(true);

				mouseButtonXAndYAxis.setDisable(true);

				a0Button.setDisable(true);

				a1Button.setDisable(true);

				a0A1Button.setDisable(true);
				
				resetButton.setDisable(true);
			});
		} else {
			mouseControlXAxisSelected = false;
			Platform.runLater(() -> {
				mouseButtonXAxis.setText("Mouse X Axis + Left Click");

				mouseButtonYAxis.setDisable(false);

				mouseButtonXAndYAxis.setDisable(false);

				a0Button.setDisable(false);

				a1Button.setDisable(false);

				a0A1Button.setDisable(false);
				
				resetButton.setDisable(false);
			});
		}
	}

	/**
	 * Mouse Y Axis Action This method runs the action to be performed follow mouse
	 * x button clicked
	 */
	private void mouseButtonYAxisActoin() {
		// clicked so change to true if false
		if (!mouseControlYAxisSelected) {
			mouseControlYAxisSelected = true;
			Platform.runLater(() -> {
				mouseButtonYAxis.setText("SELECTED :Mouse Y Axis + Left Click");

				mouseButtonXAxis.setDisable(true);

				mouseButtonXAndYAxis.setDisable(true);

				a0Button.setDisable(true);

				a1Button.setDisable(true);

				a0A1Button.setDisable(true);
				
				resetButton.setDisable(true);
			});
		} else {
			mouseControlYAxisSelected = false;
			Platform.runLater(() -> {
				mouseButtonYAxis.setText("Mouse Y Axis + Left Click");

				mouseButtonXAxis.setDisable(false);

				mouseButtonXAndYAxis.setDisable(false);

				a0Button.setDisable(false);

				a1Button.setDisable(false);

				a0A1Button.setDisable(false);
				
				resetButton.setDisable(false);
			});
		}
	}

	/**
	 * Mouse X & Y Axis Action This method runs the action to be performed follow
	 * mouse x & y button clicked
	 */
	private void mouseButtonXAndYAxisAction() {
		// clicked so change to true if false
		if (!mouseControlXAndYAxisSelected) {
			mouseControlXAndYAxisSelected = true;
			Platform.runLater(() -> {
				mouseButtonXAndYAxis.setText("SELECTED :Mouse X & Y Axis\nPress Enter to Stop");

				mouseButtonXAxis.setDisable(true);

				mouseButtonYAxis.setDisable(true);

				a0Button.setDisable(true);

				a1Button.setDisable(true);

				a0A1Button.setDisable(true);
				
				resetButton.setDisable(true);
			});
		} else {
			mouseControlXAndYAxisSelected = false;
			Platform.runLater(() -> {
				mouseButtonXAndYAxis.setText("Mouse X & Y Axis");

				mouseButtonXAxis.setDisable(false);

				mouseButtonYAxis.setDisable(false);

				a0Button.setDisable(false);

				a1Button.setDisable(false);

				a0A1Button.setDisable(false);
				
				resetButton.setDisable(false);
			});
		}
	}

	/**
	 * Connect Button Action This method runs the actions required when the connect
	 * button is clicked
	 */
	private void connectButtonAction() {
		// Set values of key listener and pass to controller to speak with model
		keySelectionListener.setKeySelection(a0Key, a1Key, a01Key, mouseControlXAxisSelected, mouseControlYAxisSelected,
				mouseControlXAndYAxisSelected, connectButton.getText());

		if (connectButton.getText().equalsIgnoreCase("Connect")) {
			Platform.runLater(() -> {
				connectButton.setText("Disconnect");
				
				connectButton.setStyle("-fx-background-color:#ff2929;");

				a0Button.setDisable(true);

				a1Button.setDisable(true);

				a0A1Button.setDisable(true);

				mouseButtonYAxis.setDisable(true);

				mouseButtonXAxis.setDisable(true);

				mouseButtonXAndYAxis.setDisable(true);
				
				resetButton.setDisable(true);
			});

			// Check if X&Y is selected if so set flag to true
			// This is required at this level at Connect Button because if it is only
			// at initial click of X&Y it will not be re-triggered once disconnected
			if (mouseButtonXAndYAxis.getText().equalsIgnoreCase("SELECTED :Mouse X & Y Axis\nPress Enter to Stop")) {
				flagConnectDisable = true;
			}

		} else {
			if (flagConnectDisable) {
				flagConnectDisable = false;
			}
			Platform.runLater(() -> {
				connectButton.setText("Connect");
				
				connectButton.setStyle(null);

				a0Button.setDisable(false);

				a1Button.setDisable(false);

				a0A1Button.setDisable(false);

				mouseButtonYAxis.setDisable(false);

				mouseButtonXAxis.setDisable(false);

				mouseButtonXAndYAxis.setDisable(false);
				
				resetButton.setDisable(false);
			});

			// check if mouse button if active is so place other buttons disabled
			if (mouseButtonXAxis.getText().equalsIgnoreCase("SELECTED :Mouse X Axis + Left Click")) {
				Platform.runLater(() -> {
					mouseButtonYAxis.setDisable(true);

					mouseButtonXAndYAxis.setDisable(true);

					a0Button.setDisable(true);

					a1Button.setDisable(true);

					a0A1Button.setDisable(true);
					
					resetButton.setDisable(true);
				});
			}

			// check if mouse button if active is so place other buttons disabled
			if (mouseButtonYAxis.getText().equalsIgnoreCase("SELECTED :Mouse Y Axis + Left Click")) {
				Platform.runLater(() -> {
					mouseButtonXAxis.setDisable(true);

					mouseButtonXAndYAxis.setDisable(true);

					a0Button.setDisable(true);

					a1Button.setDisable(true);

					a0A1Button.setDisable(true);
					
					resetButton.setDisable(true);
				});
			}

			// check if mouse button if active is so place other buttons disabled
			if (mouseButtonXAndYAxis.getText().equalsIgnoreCase("SELECTED :Mouse X & Y Axis\nPress Enter to Stop")) {
				Platform.runLater(() -> {
					mouseButtonXAxis.setDisable(true);

					mouseButtonYAxis.setDisable(true);

					a0Button.setDisable(true);

					a1Button.setDisable(true);

					a0A1Button.setDisable(true);
					
					resetButton.setDisable(true);
				});
			}
		}
	}
	
	/**
	 * Resets the KeyButtons Text
	 */
	public void resetKeyButtons() {
		
		//reset labels
		a0Label.setText("A0:");

		a1Label.setText("A1:");

		a0A1Label.setText("A0+1:");
		
		//reset key values to null
		//when connect clicked again if a key is not set null will be sent to model through controller
		a0Key = null;
		a1Key = null;
		a01Key = null;
		
	}

}
