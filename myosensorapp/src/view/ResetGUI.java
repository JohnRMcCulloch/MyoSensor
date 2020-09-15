/**
 * 
 */
package view;

import controller.ResetSceneListener;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

/**
 * @author johnmcculloch
 * Creates the pane containing the reset button
 */
public class ResetGUI extends Pane {

	//Instance Variables
	/**
	 * Reset Button
	 */
	Button resetButton;
	
	/**
	 * Reset Scene Listener
	 */
	ResetSceneListener resetSceneListener;
	
	//Constructors
	/**
	 * Default Constructor
	 */
	public ResetGUI() {
		
		resetButton = new Button("Reset");
		resetButton.setOnAction(e->{
			//reset on button click
			resetSceneListener.resetScene();
		});
		
		//add button to pane
		setPadding(new Insets(10,10,10,10));
		setMaxHeight(5);
		getChildren().add(resetButton);
		
	}

	//Getters and Setters
	/**
	 * @param resetSceneListener the resetSceneListener to set
	 */
	public void setResetSceneListener(ResetSceneListener resetSceneListener) {
		this.resetSceneListener = resetSceneListener;
	}
	
	

	

}
