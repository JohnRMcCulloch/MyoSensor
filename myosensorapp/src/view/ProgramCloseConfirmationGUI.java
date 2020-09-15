/**
 * 
 */
package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author johnmcculloch
 * Program Close Confirmation GUI
 * Class sets up components to create the program close confirmation GUI
 */
public class ProgramCloseConfirmationGUI extends Stage {

	// Instance variables
	/**
	 * Label to ask user do they want to exit program
	 */
	private Label label;

	/**
	 * Yes no button
	 */
	private Button yesButton, noButton;

	/**
	 * Layout VBox
	 */
	private VBox vBox;

	/**
	 * Scene scene
	 */
	private Scene scene;
	
	/**
	 * Response from question to close window
	 */
	static boolean answer;

	// Constructors

	/**
	 * Default Constructor
	 */
	public ProgramCloseConfirmationGUI() {

		// block user interaction with other windows
		initModality(Modality.APPLICATION_MODAL);
		setTitle("Exit Program");
		setMinWidth(250);

		// Label
		label = new Label();
		label.setFont(new Font("Arial", 15));

		// Yes No Button
		yesButton = new Button("Yes");
		noButton = new Button("No");

		yesButton.setOnAction(e -> {
			answer = true;
			close();
		});
		
		noButton.setOnAction(e->{
			answer = false;
			close();
		});

		// Layout
		vBox = new VBox(10);
		vBox.getChildren().addAll(label, yesButton, noButton);
		vBox.setAlignment(Pos.CENTER);
		vBox.setPadding(new Insets(10, 10, 10, 10));

		scene = new Scene(vBox);
		setScene(scene);
		//make sure window is always showing on top
		setAlwaysOnTop(true);
	}

	// Methods
	public boolean checkUserWantsToCloseProgram(String windowMessage) {
		label.setText(windowMessage);
		showAndWait();
		return answer;
	}

	
	
}
