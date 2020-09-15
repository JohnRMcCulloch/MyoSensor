/**
 * 
 */
package view;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import controller.ChangeSceneListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * @author johnmcculloch
 * Welcome GUI
 * Class sets up components to create the Welcome GUI
 */
public class WelcomeGUI extends BorderPane {

	//Instance Variables
	private Label welcomeLabel, infoLabel, setupLabel;
	
	private VBox vBox;
	
	private HBox hBox;
	
	private ScrollPane scrollPane;
	
	private Font fontTitle, fontSubTitle, fontText;
	
	private Button nextButton;
	
	private Image image;
	
	private ImageView imageView;
	
	private ChangeSceneListener changeSceneListener;
	
	private static String readInText = "";
	
	private final static String imgFile = "hand.jpg";
	
	private final static String arduinoSetUpFile = "arduinoSetUp.txt";
	
	//Constructors
	
	/**
	 * Default Constructor
	 */
	public WelcomeGUI() {
		
				arduinoSetUpText();
		
				//Font
				fontTitle = new Font("Arial", 30);
				fontSubTitle = new Font("Arial", 20);
				fontText = new Font("Arial", 15);
				
				//Welcome Label
				welcomeLabel = new Label("Welcome to MyoSensor");
				welcomeLabel.setFont(fontTitle);
				welcomeLabel.setPadding(new Insets(10,10,10,10));
				
				//Info Label
				infoLabel = new Label("Please ensure your arduino is set up following\n the guidelines provided, before proceeding to user details.");
				infoLabel.setFont(fontSubTitle);
				infoLabel.setPadding(new Insets(10,10,10,10));
				infoLabel.setTextAlignment(TextAlignment.CENTER);
				
				//SetUpLabel
				setupLabel = new Label(readInText);
				setupLabel.setFont(fontText);
				
				vBox = new VBox();
				
				vBox.setPadding(new Insets(10,10,10,10));
				vBox.setAlignment(Pos.CENTER);
				vBox.getChildren().addAll(welcomeLabel, infoLabel);
				vBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
				
				//Image
				image = new Image(imgFile);
				imageView = new ImageView(image);
				imageView.setFitHeight(400);
				imageView.setFitWidth(190);
				
				//ScrollPane to scroll through instructions
				scrollPane = new ScrollPane();
				scrollPane.setPadding(new Insets(10,10,10,10));
				scrollPane.setContent(setupLabel);
				scrollPane.setMaxSize(600, Double.MAX_VALUE);
				
				//Next Button
				nextButton = new Button("Next");
				nextButton.setMinSize(80, 60);
				nextButton.setOnAction(e->{
					changeSceneListener.changeScene();
				});
				
				hBox = new HBox();
				hBox.getChildren().addAll(nextButton);
				hBox.setPadding(new Insets(10,10,10,10));
				hBox.setAlignment(Pos.CENTER);
				
				//BorderPane
				setPadding(new Insets(20,20,20,20));
				setTop(vBox);
				setCenter(scrollPane);
				setRight(imageView);
				setBottom(hBox);
				setMinWidth(900);
	
	}
	
	//Getters and Setters
	/**
	 * @param changeSceneListener the changeSceneListener to set
	 */
	public void setChangeSceneListener(ChangeSceneListener changeSceneListener) {
		this.changeSceneListener = changeSceneListener;
	}

	//Methods
	

	/**
	 * Method displays a welcome message read from file file name: rules.txt
	 */
	public static void arduinoSetUpText() {
		File file = new File(arduinoSetUpFile);

		String line;

		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			line = br.readLine();

			while (line != null) {
				readInText += (line+"\n");
				line = br.readLine();
			}

			br.close();
			fr.close();

		} catch (FileNotFoundException e) {
			System.err.println("Arduino Set Up Text : Can't find file..");
		} catch (Exception exception) {
			System.err.println("Arduino Set Up Text : General exception..");
		}
	}

	


	

}
