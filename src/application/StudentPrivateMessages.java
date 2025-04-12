package application;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * GUI page that allows students to view, send, delete, and reply to private messages
 * exchanged with Reviewers. Also displays unread message count and supports search/sort.
 *
 * <p>Features include:</p>
 * <ul>
 *     <li>Viewing and loading message history</li>
 *     <li>Sending new messages or replying to existing ones</li>
 *     <li>Deleting sent messages</li>
 *     <li>Searching and sorting messages</li>
 *     <li>Displaying the count of unread messages</li>
 *     <li>Opening messages for a full view on double-click</li>
 * </ul>
 *
 * <p>Messages are pulled from the database using {@link DatabaseHelper}.</p>
 * 
 * @author Kylie Kim
 * @version 3.0
 * @since 2025-03-27
 */
public class StudentPrivateMessages {

    private User student;
    private DatabaseHelper dbHelper;
    private ListView<String> messageListView;
    private ObservableList<String> messages;
    private TextArea messageInput;
    private TextField recipientField, subjectField, searchField;
    private Label unreadCountLabel;

    private String replyToMessageID = null;

    /**
     * Constructs the StudentPrivateMessages UI.
     *
     * @param dbHelper a shared {@link DatabaseHelper} instance used for data access
     */
    public StudentPrivateMessages(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * Displays the private messages page for the student.
     *
     * @param primaryStage the main JavaFX stage
     * @param student      the logged-in student
     */
    public void show(Stage primaryStage, User student) {
        this.student = student;

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        unreadCountLabel = new Label("Unread Messages: " + getUnreadMessageCount());
        messages = FXCollections.observableArrayList();
        messageListView = new ListView<>(messages);
        messageListView.setPrefHeight(250);

        // Double-click to open message detail view
        messageListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedMessage = messageListView.getSelectionModel().getSelectedItem();
                if (selectedMessage != null) {
                    openMessageDetails(selectedMessage);
                }
            }
        });

        loadMessages();

        recipientField = new TextField();
        recipientField.setPromptText("Recipient Username");

        subjectField = new TextField();
        subjectField.setPromptText("Subject");

        messageInput = new TextArea();
        messageInput.setPromptText("Type your message...");
        messageInput.setWrapText(true);
        messageInput.setPrefHeight(100);

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> sendMessage());

        Button deleteButton = new Button("Delete Selected");
        deleteButton.setOnAction(e -> deleteSelectedMessage());

        Button replyButton = new Button("Reply");
        replyButton.setOnAction(e -> prepareReply());

        searchField = new TextField();
        searchField.setPromptText("Search messages...");
        searchField.textProperty().addListener((obs, oldText, newText) -> searchMessages(newText));

        ComboBox<String> sortCombo = new ComboBox<>();
        sortCombo.getItems().addAll("Sort by Date", "Sort by Recipient", "Sort by Read/Unread");
        sortCombo.setOnAction(e -> sortMessages(sortCombo.getValue()));

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            StudentHomePage home = new StudentHomePage(dbHelper);
            home.show(primaryStage, student);
        });

        root.getChildren().addAll(
                new Label("Private Messages"),
                unreadCountLabel,
                searchField,
                sortCombo,
                messageListView,
                new Label("To:"), recipientField,
                new Label("Subject:"), subjectField,
                messageInput,
                new HBox(10, sendButton, replyButton, deleteButton, backButton)
        );

        Scene scene = new Scene(root, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Student Private Messages");
        primaryStage.show();
    }

    /**
     * Loads all private messages for the current student and updates the view.
     */
    private void loadMessages() {
        messages.clear();
        ArrayList<String> msgs = dbHelper.getPrivateMessagesForStudent(student.getUserName());
        messages.addAll(msgs);
    }

    /**
     * Sends a new private message or reply, validates input fields,
     * and refreshes the message list if successful.
     */
    private void sendMessage() {
        String recipient = recipientField.getText();
        String subject = subjectField.getText();
        String body = messageInput.getText();

        if (recipient.isEmpty() || subject.isEmpty() || body.isEmpty()) {
            showAlert("Please fill out all message fields.");
            return;
        }

        boolean success = dbHelper.sendPrivateMessage(
                student.getUserName(), recipient, subject, body, replyToMessageID
        );

        if (success) {
            showAlert("Message sent successfully.");
            recipientField.clear();
            subjectField.clear();
            messageInput.clear();
            replyToMessageID = null;
            loadMessages();
            unreadCountLabel.setText("Unread Messages: " + getUnreadMessageCount());
        } else {
            showAlert("Failed to send message.");
        }
    }

    /**
     * Prepares the UI for replying to the selected message.
     * Autofills the recipient and subject fields.
     */
    private void prepareReply() {
        String selected = messageListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String recipient = extractRecipientFromMessage(selected);
        recipientField.setText(recipient);
        subjectField.setText("RE: " + extractSubjectFromMessage(selected));
        replyToMessageID = extractMessageIDFromMessage(selected);
    }

    /**
     * Deletes the currently selected message.
     */
    private void deleteSelectedMessage() {
        String selected = messageListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String messageID = extractMessageIDFromMessage(selected);
        dbHelper.deletePrivateMessage(messageID);
        loadMessages();
        unreadCountLabel.setText("Unread Messages: " + getUnreadMessageCount());
    }

    /**
     * Filters messages based on the user's search query (case-insensitive).
     *
     * @param query The search query string
     */
    private void searchMessages(String query) {
        ArrayList<String> filtered = dbHelper.getPrivateMessagesForStudent(student.getUserName())
                .stream()
                .filter(m -> m.toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));

        messages.setAll(filtered);
    }

    /**
     * Sorts the message list based on the given criterion.
     *
     * @param criterion The selected sort option
     */
    private void sortMessages(String criterion) {
        Comparator<String> comparator;

        switch (criterion) {
            case "Sort by Recipient":
                comparator = Comparator.comparing(this::extractRecipientFromMessage);
                break;
            case "Sort by Read/Unread":
                comparator = Comparator.comparing(msg -> msg.contains("[UNREAD]") ? 0 : 1);
                break;
            default: // Sort by Date
                comparator = Comparator.comparing(this::extractDateFromMessage).reversed();
        }

        FXCollections.sort(messages, comparator);
    }

    /**
     * Returns the number of unread messages for the current student.
     *
     * @return unread message count
     */
    private int getUnreadMessageCount() {
        return dbHelper.getUnreadPrivateMessageCount(student.getUserName());
    }

    // ============================
    // Utility parsing methods
    // ============================

    /**
     * Extracts the recipient from the raw message string.
     *
     * @param msg raw message string
     * @return extracted recipient username
     */
    private String extractRecipientFromMessage(String msg) {
        return "reviewer1"; // TODO: implement actual parsing
    }

    /**
     * Extracts the subject from the raw message string.
     *
     * @param msg raw message string
     * @return extracted subject
     */
    private String extractSubjectFromMessage(String msg) {
        return "Help with Question"; // TODO: implement actual parsing
    }

    /**
     * Extracts the message ID from the raw message string.
     *
     * @param msg raw message string
     * @return extracted message ID
     */
    private String extractMessageIDFromMessage(String msg) {
        return "123"; // TODO: implement actual parsing
    }

    /**
     * Extracts the timestamp from the raw message string.
     *
     * @param msg raw message string
     * @return extracted {@link LocalDateTime}
     */
    private LocalDateTime extractDateFromMessage(String msg) {
        return LocalDateTime.now(); // TODO: implement actual parsing
    }

    /**
     * Opens a dialog window showing the full details of the selected message.
     *
     * @param message the message string to display
     */
    private void openMessageDetails(String message) {
        Stage dialog = new Stage();
        dialog.setTitle("Message Details");

        String[] parts = message.split("\\|");

        String fromPart    = parts.length > 0 ? parts[0].trim() : "";
        String subjectPart = parts.length > 1 ? parts[1].trim() : "";
        String bodyPart    = parts.length > 2 ? parts[2].trim() : "";
        String datePart    = parts.length > 3 ? parts[3].trim() : "";
        String idPart      = parts.length > 4 ? parts[4].trim() : "";

        StringBuilder sb = new StringBuilder();
        sb.append(fromPart).append("\n");
        sb.append("Subject: ").append(subjectPart).append("\n");
        sb.append(datePart).append("\n");
        sb.append(idPart).append("\n\n");
        sb.append("Message:\n").append(bodyPart);

        TextArea textArea = new TextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);

        VBox layout = new VBox(textArea);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 400, 300);
        dialog.setScene(scene);
        dialog.show();
    }

    /**
     * Displays a simple popup alert with the provided message.
     *
     * @param message the alert content to show
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
