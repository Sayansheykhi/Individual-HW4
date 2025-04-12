package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.scene.control.CheckBox;    // for the CheckBox
import java.util.List;                  // for the List<T> interface
import java.util.ArrayList;             // for the ArrayList<T> class
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import java.time.LocalDate;
import databasePart1.*;

public class AdminInvitationPage {
	
	/**
	 * Declaration of a DatabaseHelper object for interacting with database
	 */
	private DatabaseHelper databaseHelper;
	
	/**
	 * Declaration of a User object for tracking current user
	 */
	private User user;
	
	/**
	 * Constructor creates an instance of the AdminInvitationPage
	 * 
	 * @param databaseHelper handler for interacting with database
	 * @param user the current user operating in the system
	 */
	public AdminInvitationPage(DatabaseHelper databaseHelper, User user) {
		this.databaseHelper = databaseHelper;
		this.user = user;
	}
	
    /**
     * Declaration of an object to store the role of current user
     */
    private String selectedRole = null;
    /**
     * Displays the AdminInvitationPage in the provided primary stage.
     * 
     * @param databaseHelper object instance of the DatabaseHelper
	 * @param user The current user 
     */
    public void show(Stage primaryStage, User user) {

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center; -fx-background-color: #36393F;");

        // Title
        Label title = new Label("");
        title.setStyle("-fx-font-size: 18px; -fx-text-fill: #FFFFFF;");

        // Email label (white) + asterisk (red)
        Text emailLabelText = new Text("Email ");
        emailLabelText.setFill(Color.WHITE);
        emailLabelText.setStyle("-fx-font-size: 16px;"
        		+ "-fx-translate-x: 255;"
        		+ "-fx-translate-y: 5;");

        Text asterisk = new Text("*");
        asterisk.setFill(Color.RED);
        asterisk.setStyle("-fx-font-size: 16px;"
        		+ "-fx-translate-x: 255;"
        		+ "-fx-translate-y: 3;");

        TextFlow emailLabelFlow = new TextFlow(emailLabelText, asterisk);

        // Email field
        TextField emailField = new TextField();
        emailField.setPromptText("Email address");
        emailField.setMaxWidth(250);
        emailField.setStyle("-fx-font-size: 14px; -fx-background-color: #1f1f1f; -fx-text-fill: #FFFFFF;");

        // Label for email validation or other messages
        Label validationLabel = new Label("");
        validationLabel.setStyle("-fx-text-fill: red;");

        // As user types, validate email (optional, if you’re using EmailEvaluator FSM)
        emailField.textProperty().addListener((obs, oldValue, newValue) -> {
            String trimmed = newValue.trim();
            String validationResult = EmailEvaluator.evaluateEmail(trimmed);

            if (validationResult.isEmpty()) {
                // Valid email
                validationLabel.setText("");
                emailField.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF;");
            } else {
                // Invalid
                validationLabel.setText(validationResult);
                emailField.setStyle("-fx-background-color: #300000; -fx-text-fill: #FFFFFF;");
            }
        });

        CheckBox adminCheck = new CheckBox("Admin");
        CheckBox studentCheck  = new CheckBox("Student");
        CheckBox reviewerCheck  = new CheckBox("Reviewer");
        CheckBox instructorCheck  = new CheckBox("Instructor");
        CheckBox staffCheck  = new CheckBox("Staff");

        adminCheck.setTextFill(Color.WHITE);
        studentCheck.setTextFill(Color.WHITE);
        reviewerCheck.setTextFill(Color.WHITE);
        instructorCheck.setTextFill(Color.WHITE);
        staffCheck.setTextFill(Color.WHITE);

        HBox rolesBox = new HBox(10);
        rolesBox.setAlignment(Pos.CENTER);
       // rolesBox.setStyle("-fx-translate-x: 350; ");
        rolesBox.getChildren().addAll(adminCheck, studentCheck, reviewerCheck, instructorCheck, staffCheck);

        Label deadlineLabel = new Label("Deadline:");
        deadlineLabel.setStyle("-fx-text-fill: white;");

        DatePicker deadlinePicker = new DatePicker();
        deadlinePicker.setPromptText("Select Deadline");
        deadlinePicker.setStyle("-fx-background-color: #1f1f1f; -fx-text-fill: white;");

        // Button to actually generate the invitation code
        Button generateButton = new Button("Generate Invitation Code");
        Label inviteCodeLabel = new Label();
        inviteCodeLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-style: italic;");

        generateButton.setOnAction(e -> {
            String email = emailField.getText().trim();

            // 1) Check if email is empty
            if (email.isEmpty()) {
                inviteCodeLabel.setText("Please enter a valid email.");
                inviteCodeLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            // 2) Check if email is valid
            LocalDate deadline = deadlinePicker.getValue();

            String validationResult = EmailEvaluator.evaluateEmail(email);
            if (!validationResult.isEmpty()) {
                inviteCodeLabel.setText("Invalid email: " + validationResult);
                inviteCodeLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            // 3) Check if a role was selected
            List<String> selectedRoles = new ArrayList<>();
            boolean[] selectedRole = new boolean[5];
            if (adminCheck.isSelected()) {
                selectedRoles.add("ADMIN");
                selectedRole[0] = true;
            }
            if (studentCheck.isSelected()) {
                selectedRoles.add("STUDENT");
                selectedRole[1] = true;
            }
            if (reviewerCheck.isSelected()) {
                selectedRoles.add("REVIEWER");
                selectedRole[2] = true;
            }
            if (instructorCheck.isSelected()) {
                selectedRoles.add("INSTRUCTOR");
                selectedRole[3] = true;
            }
            if (staffCheck.isSelected()) {
                selectedRoles.add("STAFF");
                selectedRole[4] = true;
            }
            boolean isEmpt = true;
            for(boolean b : selectedRole) {
            	if(b) {isEmpt = false;}
            }
            if (isEmpt) {
                inviteCodeLabel.setText("Please select at least one role before generating a code.");
                inviteCodeLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            if (deadline == null) {
                inviteCodeLabel.setText("Please select a deadline date!");
                inviteCodeLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            // 4) Convert to a comma‐separated string, e.g. "ADMIN,STUDENT"
            String rolesCsv = String.join(",", selectedRoles);


            // 4) Generate the code for whichever role is selected
            String code = databaseHelper.generateInvitationCode(email, selectedRole, deadline);

            // 5) Display it
            inviteCodeLabel.setText("Generated code: " + code + "\nRoles: " + rolesCsv +  "\nDeadline: " + deadline.toString());
            inviteCodeLabel.setStyle("-fx-text-fill: #FFFFFF;");
        });

        // Back button
        Button backButton = new Button("Back to Admin Home");
        backButton.setOnAction(e -> {
            AdminHomePage adminHome = new AdminHomePage(databaseHelper, user);
            adminHome.show(primaryStage, this.user);
        });

        // Add everything to the layout
        layout.getChildren().addAll(
            emailLabelFlow,
            emailField,
            validationLabel,
            rolesBox,
            deadlineLabel, deadlinePicker, 
            generateButton,
            inviteCodeLabel,
            backButton
            );

        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Invitation Page");
    }
}