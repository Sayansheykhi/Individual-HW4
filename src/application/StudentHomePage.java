package application;

import java.util.ArrayList;
import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;

/**
 * Displays the student home page with buttons to navigate to private messages,
 * trusted reviewers, and Q&A functionality.
 * The page also allows logging out and returning to the login screen.
 * 
 * @author Kylie Kim
 * @version 3.0
 * @since 2025-03-27
 */
public class StudentHomePage {
	
	/** Reference to the database helper for data access. */
    private final DatabaseHelper databaseHelper;

    /** The currently logged-in student user. */
    private User user;

    /**
     * Constructs the {@code StudentHomePage} with an active {@code DatabaseHelper}.
     * 
     * @param databaseHelper the database helper instance for performing operations
     */
    public StudentHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
	
    /**
     * Displays the student home screen with navigation buttons for private messages,
     * trusted reviewers, and Q&A functionality.
     *
     * @param primaryStage the JavaFX stage to display the scene
     * @param user         the logged-in student user
     */
	public void show(Stage primaryStage, User user) {
	    this.user = user;
	    
	    VBox mainLayout = new VBox(15);
	    mainLayout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    Label welcomeLabel = new Label("Welcome, Student!");
	    welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
	    
	    // Navigation Buttons
	    Button privateMessagesButton = new Button("View Private Messages");
	    privateMessagesButton.setOnAction(e -> openPrivateMessages(primaryStage));

	    Button trustedReviewersButton = new Button("Trusted Reviewers");
	    trustedReviewersButton.setOnAction(e -> openTrustedReviewers(primaryStage));

	    Button questionsAndAnswersButton = new Button("Questions and Answers");
	    questionsAndAnswersButton.setOnAction(e -> openQuestionsAndAnswers(primaryStage));
	    
	    Button newRoleRequestButton = new Button("Request New Role(s)");
	    newRoleRequestButton.setOnAction(e -> openRoleRequest(primaryStage));

	    // Questions and Answers Section
	    HBox contentLayout = new HBox(20);
	    contentLayout.setStyle("-fx-padding: 20;");

	    Button logoutButton = new Button("Logout");
	    logoutButton.setOnAction(a -> confirmLogout(primaryStage));

	    mainLayout.getChildren().addAll(
	        welcomeLabel,
	        privateMessagesButton,
	        trustedReviewersButton,
	        questionsAndAnswersButton,
	        newRoleRequestButton,
	        contentLayout,
	        logoutButton
	    );

	    Scene studentScene = new Scene(mainLayout, 1200, 600);
	    primaryStage.setScene(studentScene);
	    primaryStage.setTitle("Student Home Page");
	    primaryStage.show();
	}

	/**
     * Opens the {@code StudentPrivateMessages} page.
     * 
     * @param primaryStage the stage on which the new page is shown
     */
	private void openPrivateMessages(Stage primaryStage) {
	    StudentPrivateMessages messagesPage = new StudentPrivateMessages(databaseHelper);
	    messagesPage.show(primaryStage, this.user);
	}

	/**
     * Opens the {@code StudentTrustedReviewersList} page.
     *
     * @param primaryStage the stage on which the new page is shown
     */
	private void openTrustedReviewers(Stage primaryStage) {
	    StudentTrustedReviewersList reviewersPage;
	    reviewersPage = new StudentTrustedReviewersList(databaseHelper);
	    reviewersPage.show(primaryStage, this.user);
	}

	/**
     * Opens the {@code StudentQuestionsAnswers} page.
     *
     * @param primaryStage the stage on which the new page is shown
     */
	private void openQuestionsAndAnswers(Stage primaryStage) {
	    StudentQuestionsAnswers qaPage = new StudentQuestionsAnswers(databaseHelper);
	    qaPage.show(primaryStage, this.user);
	}
	
	/**
     * Opens the {@code RequestNewRole} page.
     *
     * @param primaryStage the stage on which the new page is shown
     */
	private void openRoleRequest(Stage primaryStage) {
	    RequestNewRole requestNewRolePage = new RequestNewRole(databaseHelper);
	    requestNewRolePage.show(primaryStage, this.user);
	}

	/**
     * Prompts the user with a logout confirmation dialog.
     * 
     * @param primaryStage the current stage to return to login if confirmed
     */
	private void confirmLogout(Stage primaryStage) {
	    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	    alert.setTitle("Logout Confirmation");
	    alert.setHeaderText("Please confirm if you're logging out.");
	    alert.setContentText("Make sure all changes are saved.");

	    alert.getButtonTypes().setAll(new ButtonType("Save and Logout"), new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE));
	    alert.showAndWait().ifPresent(response -> logout(primaryStage));
	}
	
	/**
     * Logs out the current user and navigates to the {@code UserLoginPage}.
     *
     * @param primaryStage the current stage
     */
	private void logout(Stage primaryStage) {
	    new UserLoginPage(databaseHelper).show(primaryStage, this.user);
	}
}