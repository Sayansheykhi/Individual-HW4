package application;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


/**
 * <p><strong>ReviewerPrivateMessages</strong> provides a messaging interface
 * for a <em>Reviewer</em> to communicate privately with Students or other roles.
 * It supports sending new messages, listing existing messages (including search
 * and filtering), and replying to or deleting messages.</p>
 *
 * <p>Messages are tied to a database for persistence, which is handled via
 * the {@link databasePart1.DatabaseHelper} instance passed in the constructor.</p>
 *
 * @author Sajjad
 */
public class ReviewerPrivateMessages {
	/**
     * A reference to the database helper for performing DB operations (saving and loading messages).
     */
    private final DatabaseHelper databaseHelper;
    
    /**
     * The current user (Reviewer) viewing and sending messages.
     */
    private User reviewer;
    
    /**
     * An observable list of messages that will be displayed in the ListView.
     */
    private final ObservableList<ReviewerMessage> messages = FXCollections.observableArrayList();
    
    /**
     * The ListView UI component showing the messages.
     */
    private ListView<ReviewerMessage> messageListView;
    
    /**
     * A date-time formatter used for displaying human-readable timestamps.
     */
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Display the Reviewer Private Messages page.
     * @param primaryStage The stage on which the scene is set.
     * @param reviewer The current Reviewer user.
     */
    public ReviewerPrivateMessages(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    
    /**
     * Displays the reviewer private messages UI in a new scene.
     * 
     * @param primaryStage The stage on which this scene will be set.
     * @param reviewer     The {@link User} object representing the currently logged-in Reviewer.
     */
    public void show(Stage primaryStage, User reviewer) {
        this.reviewer = reviewer;
        
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        
        Label header = new Label("Reviewer Private Messages");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        // Top section with a search field
        TextField searchField = new TextField();
        searchField.setPromptText("Search by recipient, subject, or date");
        HBox topBox = new HBox(10, header, searchField);
        topBox.setPadding(new Insets(10));
        topBox.setAlignment(Pos.CENTER);
        root.setTop(topBox);
        
        // Center: Initialize the ListView (as an instance field)
        messageListView = new ListView<>();
        FilteredList<ReviewerMessage> filteredMessages = new FilteredList<>(messages, m -> true);
        messageListView.setItems(filteredMessages);
        messageListView.setCellFactory(lv -> new ListCell<ReviewerMessage>() {
            @Override
            protected void updateItem(ReviewerMessage message, boolean empty) {
                super.updateItem(message, empty);
                if (empty || message == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String text = String.format("To: %s (%s)\nSubject: %s\nSent: %s\n%s",
                            message.getRecipient(),
                            message.getRecipientRole(),
                            message.getSubject(),
                            dtf.format(message.getSentTime()),
                            message.getIsRead() ? "[Read]" : "[Unread]");
                    setText(text);
                }
            }
        });
        
        // Double-click to open message details
        messageListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                ReviewerMessage selected = messageListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    showMessageDetails(selected);
                }
            }
        });
        root.setCenter(messageListView);
        
        // Search filtering
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredMessages.setPredicate(message -> {
                if (newVal == null || newVal.trim().isEmpty()) return true;
                String lower = newVal.toLowerCase();
                return message.getRecipient().toLowerCase().contains(lower)
                        || message.getSubject().toLowerCase().contains(lower)
                        || dtf.format(message.getSentTime()).toLowerCase().contains(lower);
            });
        });
        
        // Right: Form to send a new message
        VBox sendMessageBox = new VBox(10);
        sendMessageBox.setPadding(new Insets(10));
        sendMessageBox.setStyle("-fx-border-color: grey; -fx-border-width: 1;");
        
        Label newMsgLabel = new Label("Send New Message");
        newMsgLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        TextField recipientField = new TextField();
        recipientField.setPromptText("Recipient username");
        TextField recipientRoleField = new TextField();
        recipientRoleField.setPromptText("Recipient role (e.g., Student)");
        TextField subjectField = new TextField();
        subjectField.setPromptText("Subject");
        TextArea messageBodyArea = new TextArea();
        messageBodyArea.setPromptText("Message body");
        messageBodyArea.setPrefRowCount(4);
        
        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            String recipient = recipientField.getText().trim();
            String recipientRole = recipientRoleField.getText().trim();
            String subject = subjectField.getText().trim();
            String body = messageBodyArea.getText().trim();
            if (recipient.isEmpty() || subject.isEmpty() || body.isEmpty()) {
                showAlert("Error", "Please fill in all fields for the new message.");
                return;
            }
            
            // Create a new message; pass -1 as default reviewID if not applicable.
            // pass -1 as temp messageID, database to create permanent messageID
            ReviewerMessage newMsg = new ReviewerMessage(
                    -1,
                    reviewer.getUserName(),
                    recipient,
                    recipientRole,
                    subject,
                    body,
                    LocalDateTime.now(),
                    false,
                    -1
            );
            databaseHelper.saveReviewerMessage(newMsg);
            messages.add(newMsg);
            recipientField.clear();
            recipientRoleField.clear();
            subjectField.clear();
            messageBodyArea.clear();
        });
        
        sendMessageBox.getChildren().addAll(newMsgLabel, new Label("Recipient:"), recipientField,
                new Label("Recipient Role:"), recipientRoleField,
                new Label("Subject:"), subjectField,
                new Label("Message:"), messageBodyArea, sendButton);
        root.setRight(sendMessageBox);
        
        // Bottom: Buttons for Delete, Reply, and Back
        Button deleteButton = new Button("Delete Selected Message");
        deleteButton.setOnAction(e -> {
            ReviewerMessage selected = messageListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                messages.remove(selected);
            } else {
                showAlert("Delete Error", "Please select a message to delete.");
            }
        });
        
        Button replyButton = new Button("Reply to Selected Message");
        replyButton.setOnAction(e -> {
            ReviewerMessage selected = messageListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                openReplyDialog(primaryStage, selected);
            } else {
                showAlert("Reply Error", "Please select a message to reply to.");
            }
        });
        
        Button backButton = new Button("Back to Reviewer Home");
        backButton.setOnAction(e -> {
            new ReviewerHomePage(databaseHelper).show(primaryStage, reviewer);
        });
        
        HBox bottomBox = new HBox(10, deleteButton, replyButton, backButton);
        bottomBox.setPadding(new Insets(10));
        bottomBox.setAlignment(Pos.CENTER);
        root.setBottom(bottomBox);
        
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Reviewer Private Messages");
        primaryStage.show();
    }
    
    /**
     * Opens a simple dialog window displaying the full message details of the selected message.
     * 
     * @param message The {@link ReviewerMessage} object whose details are shown.
     */
    private void showMessageDetails(ReviewerMessage message) {
        if (!message.getIsRead()) {
            message.setRead(true);
            databaseHelper.markReviewerMessageAsRead(message.getMessageID(), message);
            // Refresh the ListView so the updated read status is displayed.
            // Since messageListView is not passed here, you might need to ensure it is a class field.
            // For example, if you had declared it as: private ListView<ReviewerMessage> messageListView;
            // then you can call: messageListView.refresh();
        }
        
        Stage dialog = new Stage();
        dialog.setTitle("Message Details");
        
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        
        Label fromLabel = new Label("From: " + message.getSender());
        Label subjectLabel = new Label("Subject: " + message.getSubject());
        Label sentTimeLabel = new Label("Sent: " + dtf.format(message.getSentTime()));
        Label statusLabel = new Label(message.getIsRead() ? "[Read]" : "[Unread]");
        
        TextArea bodyArea = new TextArea(message.getBody());
        bodyArea.setEditable(false);
        bodyArea.setWrapText(true);
        
        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> dialog.close());
        
        layout.getChildren().addAll(fromLabel, subjectLabel, sentTimeLabel, statusLabel, bodyArea, closeBtn);
        
        Scene scene = new Scene(layout, 400, 300);
        dialog.setScene(scene);
        dialog.show();
    }
    
    /**
     * Opens a dialog allowing the reviewer to write a reply to an existing message.
     *
     * @param owner           The parent stage for the dialog.
     * @param originalMessage The original message being replied to.
     */
    private void openReplyDialog(Stage owner, ReviewerMessage originalMessage) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.setTitle("Reply to Message");
        
        VBox dialogVBox = new VBox(10);
        dialogVBox.setPadding(new Insets(20));
        
        Label info = new Label("Replying to: " + originalMessage.getSubject());
        TextArea replyArea = new TextArea();
        replyArea.setPromptText("Type your reply here...");
        replyArea.setPrefRowCount(4);
        
        Button sendReplyButton = new Button("Send Reply");
        sendReplyButton.setOnAction(e -> {
            String replyText = replyArea.getText().trim();
            if (replyText.isEmpty()) {
                showAlert("Error", "Reply message cannot be empty.");
                return;
            }
            String replySubject = "Re: " + originalMessage.getSubject();
            ReviewerMessage replyMsg = new ReviewerMessage(
                    originalMessage.getMessageID(),
                    reviewer.getUserName(),
                    originalMessage.getRecipient(),
                    originalMessage.getRecipientRole(),
                    replySubject,
                    replyText,
                    LocalDateTime.now(),
                    false,
                    originalMessage.getReviewID()
            );
            databaseHelper.saveReviewerMessage(replyMsg);
            messages.add(replyMsg);
            dialog.close();
        });
        
        dialogVBox.getChildren().addAll(info, replyArea, sendReplyButton);
        Scene dialogScene = new Scene(dialogVBox, 400, 300);
        dialog.setScene(dialogScene);
        dialog.show();
    }
    
    /**
     * Displays an alert pop-up with the specified title and message.
     *
     * @param title   A short string describing the alert (e.g., "Error")
     * @param message The alert details or instructions to the user.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}