/**
 * 
 */
package view;

import controller.ChangeSceneDataListener;
import controller.NotesDetailsListener;
import controller.PatientIDDetailsListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

/**
 * @author johnmcculloch
 * User Details GUI
 * Class sets up components to create the user details main GUI
 */
public class UserDetailsGUI extends GridPane {

	// Instance variables

	/**
	 * Button
	 */
	private Button nextButton;

	/**
	 * Label used to highlight user input
	 */
	private Label patientIDLabel, amputationSideLabel, notesLabel, arduinoSetUpLabel;

	/**
	 * Patient ID Input textFeild
	 */
	private TextField patientIDInput;

	/**
	 * Toggle Group, contains Left or Right RadioButtons for Amputation side
	 */
	private ToggleGroup amputationSideGroup;

	/**
	 * RadioButtons for amputation side
	 */
	private RadioButton leftRB, rightRB;

	/**
	 * User notes textArea input
	 */
	private TextArea notesInput;

	/**
	 * Arduino Set Up check box (used to allow user to progress)
	 */
	private CheckBox arduinoSetUp;

	/**
	 * Change Scene Listener
	 */
	private ChangeSceneDataListener changeSceneDataListener;

	/**
	 * Patient ID max limit of chars 20 As stated in UserDetails business rules
	 */
	final int PATIENTID_MAX_LIMIT_CHARS = 20;

	/**
	 * PatientID Details Listener
	 */
	private PatientIDDetailsListener patientIDDetailsListener;

	/**
	 * Notes Listener
	 */
	private NotesDetailsListener notesDetailsListener;

	/**
	 * Font
	 */
	private Font font;

	// Constructors

	/**
	 * Default Constructor
	 */
	public UserDetailsGUI() {

		// Font
		font = new Font("Arial", 15);

		// Patient ID
		patientIDLabel = new Label("Patient ID : ");
		patientIDLabel.setFont(font);
		patientIDInput = new TextField();
		patientIDInput.setPromptText("Patient ID");
		// According to UserDetails patientID can not be longer than 20chars make sure
		// user not able to type
		// more than 20
		patientIDInput.setTextFormatter(new TextFormatter<String>(
				change -> change.getControlNewText().length() <= PATIENTID_MAX_LIMIT_CHARS ? change : null));
		GridPane.setConstraints(patientIDLabel, 0, 0);
		GridPane.setConstraints(patientIDInput, 1, 0);

		// Amputated Side (Testing Side)
		amputationSideLabel = new Label("Side of residual limb : ");
		amputationSideLabel.setFont(font);
		GridPane.setConstraints(amputationSideLabel, 0, 1);

		amputationSideGroup = new ToggleGroup();

		leftRB = new RadioButton("Left");
		leftRB.setToggleGroup(amputationSideGroup);

		rightRB = new RadioButton("Right");
		rightRB.setToggleGroup(amputationSideGroup);

		HBox radioButtons = new HBox();
		radioButtons.setPadding(new Insets(10, 10, 10, 10));
		radioButtons.setSpacing(20);
		radioButtons.getChildren().addAll(leftRB, rightRB);
		GridPane.setConstraints(radioButtons, 1, 1);

		// Notes
		notesLabel = new Label("Notes : ");
		notesLabel.setFont(font);
		GridPane.setConstraints(notesLabel, 0, 2);

		notesInput = new TextArea();
		notesInput.setPromptText("notes...");
		
		/* Business rule - removed code not required.
		
		// According to UserDetails notes can not be longer than 500chars make sure user
		// not able to type
		// more than 500
		notesInput.setTextFormatter(new TextFormatter<String>(
				change -> change.getControlNewText().length() <= NOTES_MAX_LIMIT_CHARS ? change : null));
		
		 */
		 GridPane.setConstraints(notesInput, 1, 3);
		
		

		// Confirm arduino is set up
		arduinoSetUpLabel = new Label("Arduino Set Up :");
		arduinoSetUpLabel.setFont(font);
		GridPane.setConstraints(arduinoSetUpLabel, 0, 4);

		arduinoSetUp = new CheckBox("Confirm");
		GridPane.setConstraints(arduinoSetUp, 1, 4);

		// Next Button (On to graph)
		nextButton = new Button("Start Training");
		GridPane.setConstraints(nextButton, 1, 6);
		// nextButton.setDisable(true);
		nextButton.setOnAction(e -> {
			
		
			changeSceneDataListener.changeScene(patientIDInput.getText(), notesInput.getText(), selectedRadioButton());
		});

		// Check if amputated side has been selected
		BooleanBinding amputationSideSelected = Bindings.createBooleanBinding(() -> {

			if (amputationSideGroup.getSelectedToggle() != null) {
				return true;
			} else {
				return false;
			}

		}, amputationSideGroup.selectedToggleProperty());

		// check if arduino set up confirmation and been checked
		BooleanBinding arduinoSetUpConfirmedChecked = Bindings.createBooleanBinding(() -> {

			if (arduinoSetUp.isSelected()) {
				return true;
			} else {
				return false;
			}

		}, arduinoSetUp.selectedProperty());

		//check patient id entered is within the business guidelines
		BooleanBinding patientIDInputCheck = Bindings.createBooleanBinding(() -> {
			return checkPatientID();
		}, patientIDInput.textProperty());

		//check notes entered is within the business guidelines
		BooleanBinding notesCheck = Bindings.createBooleanBinding(() -> {
			return checkNotes();
		}, notesInput.textProperty());

		nextButton.disableProperty().bind(patientIDInputCheck.not().or(arduinoSetUpConfirmedChecked.not())
				.or(notesCheck.not().or(amputationSideSelected.not())));

		// GridPane SetUp
		setPadding(new Insets(10, 10, 10, 10));
		setVgap(10);
		setHgap(10);
		setAlignment(Pos.CENTER_LEFT);
		getChildren().addAll(patientIDLabel, patientIDInput, amputationSideLabel, radioButtons, notesLabel, notesInput,
				arduinoSetUpLabel, arduinoSetUp, nextButton);

	}

	// Getters and Setters

	/**
	 * @param changeSceneListener the changeSceneListener to set
	 */
	public void setChangeDataSceneListener(ChangeSceneDataListener changeDataSceneListener) {
		this.changeSceneDataListener = changeDataSceneListener;
	}

	/**
	 * @param notesDetailsListener the notesDetailsListener to set
	 */
	public void setNotesDetailsListener(NotesDetailsListener notesDetailsListener) {
		this.notesDetailsListener = notesDetailsListener;
	}

	/**
	 * @param patientIDDetailsListener the patientIDDetailsListener to set
	 */
	public void setPatientIDDetailsListener(PatientIDDetailsListener patientIDDetailsListener) {
		this.patientIDDetailsListener = patientIDDetailsListener;
	}

	// Methods
	/**
	 * Check Patient ID in Controller
	 * 
	 * @return Boolean
	 */
	private boolean checkPatientID() {
		try {
			return patientIDDetailsListener.checkUserDetails(patientIDInput.getText());
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Check Notes in Controller
	 * 
	 * @return Boolean
	 */
	private boolean checkNotes() {
		try {
			return notesDetailsListener.checkNotesInUserDetails(notesInput.getText());
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Find the selected amputation side
	 * @return Side (String)
	 */
	private String selectedRadioButton() {
		
		//Convert Toggle Property back to a RadioButton
		RadioButton selectedRadioButton = (RadioButton) amputationSideGroup.getSelectedToggle();
		
		
		return selectedRadioButton.getText();
	}
	 

}
