package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * StaffPrivateFeedbackPage allows a Staff user to send private feedback
 * to any other user (student, reviewer, etc.) by inserting a message
 * into the 'PrivateMessages' table (via DatabaseHelper).
 */
public class StaffPrivateFeedbackPage {

    private DatabaseHelper databaseHelper;
    private User staffUser;  // the staff user who is logged in

    /**
     * Constructor
     * @param db        the DatabaseHelper for DB operations
     * @param user      the currently logged-in staff user
     */
    public StaffPrivateFeedbackPage(DatabaseHelper db, User user) {
        this.databaseHelper = db;
        this.staffUser = user;
    }

    /**
     * Displays the page in the given JavaFX Stage.
     * @param primaryStage the main window
     */
    public void show(Stage primaryStage) {
        // Main layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_LEFT);

        // Title
        Label titleLabel = new Label("Send Private Feedback (Staff)");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Input fields
        TextField recipientField = new TextField();
        recipientField.setPromptText("Recipient username...");

        TextField subjectField = new TextField();
        subjectField.setPromptText("Subject...");

        TextArea bodyArea = new TextArea();
        bodyArea.setPromptText("Type your feedback message here...");
        bodyArea.setPrefHeight(150);

        // Send button
        Button sendButton = new Button("Send Feedback");
        sendButton.setOnAction(e -> {
            String recipient = recipientField.getText().trim();
            String subject   = subjectField.getText().trim();
            String body      = bodyArea.getText().trim();

            // Validate fields
            if (recipient.isEmpty() || subject.isEmpty() || body.isEmpty()) {
                showAlert("Error", "Please fill in all fields before sending.");
                return;
            }

            // Attempt to insert into DB
            boolean success = databaseHelper.sendPrivateMessage(
                staffUser.getUserName(),  // sender
                recipient,               // receiver
                subject,
                body
            );

            if (success) {
                showAlert("Success", "Message sent successfully!");
                // Clear fields
                recipientField.clear();
                subjectField.clear();
                bodyArea.clear();
            } else {
                showAlert("Error", "Failed to send message. Check logs for details.");
            }
        });

        // Back button to return to staff home
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            // Return to StaffHomePage
            StaffHomePage homePage = new StaffHomePage(databaseHelper, staffUser);
            homePage.show(primaryStage, staffUser);
        });

        // Add everything to layout
        root.getChildren().addAll(
                titleLabel,
                new Label("Recipient:"), recipientField,
                new Label("Subject:"), subjectField,
                new Label("Message:"), bodyArea,
                sendButton,
                backButton
        );

        // Show scene
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Staff: Private Feedback");
        primaryStage.show();
    }

    /**
     * Helper to show an alert box
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
