package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import databasePart1.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This page allows a student to view their submitted questions, review answers, 
 * send private messages to reviewers, and request clarifications.
 * 
 * This class has been enhanced in TP3 to support:
 * - Private messaging per answer
 * - Clarification requests
 * - Display only student’s own questions (using DatabaseHelper)
 * 
 * @author Kylie Kim
 * @version 3.0
 * @since 2025-03-27
 */
public class StudentMessageFromQuestionsAnswers {

    private DatabaseHelper dbHelper;
    private User user;

    private ListView<Question> submittedQuestionsList;
    private ListView<Answer> submittedAnswerList;
    private ListView<Review> submittedReviewsList;

    private TextArea replyTextField;
    private Button saveReplyButton;
    private Label replyStatusLabel;
    
    /**
     * Constructs the StudentQuestionsAnswers page with a shared DatabaseHelper.
     *
     * @param databaseHelper the shared DatabaseHelper instance
     */
    public StudentMessageFromQuestionsAnswers(DatabaseHelper databaseHelper) {
        this.dbHelper = databaseHelper;
    }

    /**
     * Displays the Student Questions and Answers page for the given user.
     * 
     * @param primaryStage The stage to display the scene.
     * @param user The logged-in student user.
     */
    public void show(Stage primaryStage, User user) {
        this.user = user;
        
        /*dbHelper = new DatabaseHelper();
       
        try {
            dbHelper.connectToDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            // Optionally show error alert to the user
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Database Connection Failed");
            errorAlert.setContentText("Unable to connect to the database.");
            errorAlert.showAndWait();
            return; // Exit early if DB connection failed
        }
        */

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Label pageTitle = new Label("Your Submitted Questions and Answers");

        submittedQuestionsList = new ListView<>();
        submittedAnswerList = new ListView<>();
        submittedReviewsList = new ListView<>();

        loadSubmittedQuestions();
        loadSubmittedAnswers();
        loadSubmittedReviews();

        submittedQuestionsList.setPrefHeight(150);
        submittedAnswerList.setPrefHeight(150);
        submittedReviewsList.setPrefHeight(150);

        // Reply section for private messages or clarifications
        Label replyLabel = new Label("Send Private Message / Clarification to Reviewer:");
        replyTextField = new TextArea();
        replyTextField.setPromptText("Type your message here...");
        replyTextField.setWrapText(true);
        replyTextField.setPrefHeight(100);

        saveReplyButton = new Button("Send Message");
        saveReplyButton.setOnAction(e -> sendPrivateMessage());

        replyStatusLabel = new Label();

        Button backButton = new Button("Back to Home");
        backButton.setOnAction(e -> {
            StudentHomePage homePage = new StudentHomePage(dbHelper);
            homePage.show(primaryStage, user);
        });

        root.getChildren().addAll(
            pageTitle,
            new Label("Your Questions:"),
            submittedQuestionsList,
            new Label("Answers You've Received:"),
            submittedAnswerList,
            new Label("Reviews for Answers You've Received:"),
            submittedReviewsList,
            replyLabel,
            replyTextField,
            saveReplyButton,
            replyStatusLabel,
            backButton
        );

        Scene scene = new Scene(root, 600, 700);
        primaryStage.setTitle("Student Q&A");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Loads the student's submitted questions from the database.
     */
    private void loadSubmittedQuestions() {
        ArrayList<Question> questions = dbHelper.getSubmittedQuestionsByUser(user.getUserName());
        ObservableList<Question> observableQuestions = FXCollections.observableArrayList(questions);
        submittedQuestionsList.setItems(observableQuestions);
    }

    /**
     * Loads answers linked to the student's questions from the database.
     */
    private void loadSubmittedAnswers() {
        ArrayList<Answer> answers = dbHelper.getAnswersForUserQuestions(user.getUserName());
        ObservableList<Answer> observableAnswers = FXCollections.observableArrayList(answers);
        submittedAnswerList.setItems(observableAnswers);
    }
    
    /**
     * Loads reviews linked to potential answers to the student's questions from the database.
     */
    private void loadSubmittedReviews() {
    	ArrayList<Review> reviews = dbHelper.getReviewsForUserAnswers(user.getUserName());
    	ObservableList<Review> observableReviews = FXCollections.observableArrayList(reviews);
        submittedReviewsList.setItems(observableReviews);
    }

    /**
     * Sends a private message or clarification request from the student to the reviewer 
     * of the selected answer. This method performs the following:
     * <ul>
     *   <li>Validates that an answer is selected and the message is not empty</li>
     *   <li>Retrieves the reviewer's username associated with the selected answer</li>
     *   <li>Constructs a subject line using the answer ID</li>
     *   <li>Sends the message using the {@link DatabaseHelper#sendPrivateMessage} method</li>
     *   <li>Displays success or failure status in the {@code replyStatusLabel}</li>
     * </ul>
     * 
     * If no answer is selected or the message field is empty, the user is informed via the UI.
     */
    private void sendPrivateMessage() {
        Answer selectedAnswer = submittedAnswerList.getSelectionModel().getSelectedItem();
        String message = replyTextField.getText().trim();

        if (selectedAnswer == null) {
            replyStatusLabel.setText("Please select an answer to message.");
            return;
        }

        if (message.isEmpty()) {
            replyStatusLabel.setText("Message cannot be empty.");
            return;
        }

        
        String reviewerUserName = dbHelper.getReviewerUserNameByAnswerID(selectedAnswer.getAnswerID());

       
        String subject = "Message regarding Answer ID " + selectedAnswer.getAnswerID();

    
        boolean success = dbHelper.sendPrivateMessage(
            user.getUserName(),             
            reviewerUserName,              
            subject,                      
            message                           
        );

        if (success) {
            replyStatusLabel.setText("Message sent successfully!");
            replyTextField.clear();
        } else {
            replyStatusLabel.setText("Failed to send message.");
        }
    }

}