/**
 * 
 */
package view;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * @author johnmcculloch
 * User Details GUI Title
 * Class sets up components to create the user details title GUI
 */
public class UserDetailsGUITitle extends VBox {

	//Instance variables
	/**
	 * Labels to inform user to enter details
	 */
	Label labelTitle, labelInformation;
	
	
	/**
	 * default constructor 
	 */
	public UserDetailsGUITitle() {
		
		labelTitle = new Label("Please Enter User Details Below");
		labelTitle.setFont(new Font("Arial", 20));
		labelInformation = new Label("\nPlease ensure arduino is set up following guidelines");
		labelInformation.setFont(new Font("Arial", 15));
		
		setPadding(new Insets(10,10,10,10));
		setAlignment(Pos.CENTER);
		getChildren().addAll(labelTitle, labelInformation);
	}

	

}
