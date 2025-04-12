package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The ComputeReviewersScorecard handles the interface responsible for displaying and calculating the scorecards
 * for reviewers within the forum question and answer system
 * 
 * @author John Gallagher
 * @version 1.0 3/31/25
 */
public class ComputeReviewersScorecard {
	
	/**
	 * Declaration of a DatabaseHelper object for interacting with database
	 */
	private DatabaseHelper databaseHelper;
	
	/**
	 * Declaration of a User object for tracking current user
	 */
	private User user;
	
	/**
	 * Constructor creates an instance of the ComputeReviewersScorecard interface
	 * 
	 * @param databaseHelper object for handling interacting with database
	 * @param user the current user in the system
	 */
	public ComputeReviewersScorecard(DatabaseHelper databaseHelper, User user) {
		this.databaseHelper =databaseHelper;
		this.user = user;
	}
	
	/**
	 * Displays the ComputeReviewersScorecard in the provided primary stage.
	 * 
	 * @param primaryStage The primary stage where the scene will be displayed
     * @param user The current user passed from previous scene
	 */
	public void show(Stage primaryStage, User user) {
		this.user = user;
		
		VBox layout = new VBox(10);
		layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
		
		//Label to display place holder message for Instructor
		Label placeHolderLabel = new Label("Hello, Compute a Scorecard coming soon!");
		placeHolderLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
		
		//Button to return to InstructorHomePage
		Button backButton = new Button("Go Back");
		String buttonStyle = "-fx-font-size: 14px; -fx-padding: 10px; -fx-pref-width: 250px;";
		backButton.setStyle(buttonStyle);
		backButton.setOnAction(e -> returnHomePage(primaryStage));
		
		// Set the scene to primary stage
	    layout.getChildren().addAll(placeHolderLabel, backButton);
	    Scene computeScene = new Scene(layout, 800, 400);
	    primaryStage.setScene(computeScene);
	    primaryStage.setTitle("Compute Scorecard");
	}
	
	/**
	 * Redirects to InstructorHomePage
	 * 
	 * @param primaryStage the primary stage the user is in
	 */
	private void returnHomePage(Stage primaryStage) {
		InstructorHomePage instrucPage = new InstructorHomePage(databaseHelper, user);
		instrucPage.show(primaryStage, this.user);
	}
}