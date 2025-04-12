package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The {@code InstructorReviewerRequests} class provides the JavaFX user interface for instructors
 * to manage and view student-submitted questions and answers, as well as handle reviewer role requests.
 *
 * <p>Main Features:</p>
 * <ul>
 *   <li>View and filter student questions and answers</li>
 *   <li>Open question and answer details in a dialog</li>
 *   <li>Approve or deny reviewer role requests</li>
 * </ul>
 *
 * <p>This class uses {@link DatabaseHelper} to fetch data and {@link User} to represent the logged-in instructor.</p>
 * 
 * @author sajjad
 */
public class InstructorReviewerRequests {

    /**
     * Shows an error alert dialog with the specified title and message.
     *
     * @param title   the title of the alert window
     * @param message the message to display inside the alert
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /** A helper class to interact with the database. */
    private DatabaseHelper databaseHelper;

    /** The instructor (user) for whom this interface is displayed. */
    private User instructor;

    /**
     * Constructs an {@code InstructorReviewerRequests} object with the specified
     * {@link DatabaseHelper}.
     *
     * @param databaseHelper a {@link DatabaseHelper} instance used for querying and updating the database
     */
    public InstructorReviewerRequests(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the instructor interface for managing questions, answers,
     * and reviewer role requests.
     *
     * @param primaryStage the main application stage
     * @param instructor   the current instructor (user) for whom this UI is shown
     */
    public void show(Stage primaryStage, User instructor) {
        this.instructor = instructor;

        TabPane tabPane = new TabPane();

        Tab qaTab = new Tab("Questions & Answers", createQAPane());
        qaTab.setClosable(false);

        Tab requestsTab = new Tab("Reviewer Requests", createRequestsPane());
        requestsTab.setClosable(false);

        tabPane.getTabs().addAll(qaTab, requestsTab);

        Button backButton = new Button("Back to Instructor Home");
        backButton.setOnAction(e ->
                new InstructorHomePage(databaseHelper, instructor).show(primaryStage, instructor)
        );

        VBox mainLayout = new VBox(10, tabPane, backButton);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(mainLayout, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Instructor Reviewer Requests");
        primaryStage.show();
    }

    /**
     * Creates and returns a pane containing the Questions & Answers section
     * where the instructor can filter questions and answers by student username.
     *
     * @return a {@link Pane} with the Questions & Answers UI
     */
    private Pane createQAPane() {
        VBox qaLayout = new VBox(10);
        qaLayout.setPadding(new Insets(10));
        qaLayout.setAlignment(Pos.CENTER);

        Label header = new Label("Questions & Answers");
        header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        HBox sections = new HBox(20);
        sections.setAlignment(Pos.CENTER);

        VBox questionsBox = new VBox(10);
        Label questionsLabel = new Label("Questions");

        TextField filterField = new TextField();
        filterField.setPromptText("Filter by student username");

        ListView<Question> questionsList = new ListView<>();
        ObservableList<Question> allQuestions = FXCollections.observableArrayList(databaseHelper.getAllQuestions());
        questionsList.setItems(allQuestions);

        questionsList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Question selected = questionsList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    showQuestionDetails(selected);
                }
            }
        });

        questionsList.setCellFactory(lv -> new ListCell<Question>() {
            @Override
            protected void updateItem(Question q, boolean empty) {
                super.updateItem(q, empty);
                if (empty || q == null) {
                    setText(null);
                } else {
                    setText(q.getStudentUserName() + ":\n" + q.getQuestionTitle());
                }
            }
        });

        Button filterButton = new Button("Filter Questions");
        filterButton.setOnAction(e -> {
            String student = filterField.getText().trim().toLowerCase();
            questionsList.setItems(allQuestions.filtered(q -> q.getStudentUserName().toLowerCase().contains(student)));
        });

        questionsBox.getChildren().addAll(questionsLabel, filterField, questionsList, filterButton);

        VBox answersBox = new VBox(10);
        Label answersLabel = new Label("Answers");

        TextField answerFilterField = new TextField();
        answerFilterField.setPromptText("Filter by student username");

        ListView<Answer> answersList = new ListView<>();
        ObservableList<Answer> allAnswers = FXCollections.observableArrayList(databaseHelper.getAllAnswers(instructor));
        answersList.setItems(allAnswers);

        answersList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Answer selected = answersList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    showAnswerDetails(selected);
                }
            }
        });

        answersList.setCellFactory(lv -> new ListCell<Answer>() {
            @Override
            protected void updateItem(Answer a, boolean empty) {
                super.updateItem(a, empty);
                if (empty || a == null) {
                    setText(null);
                } else {
                    setText(a.getStudentUserName() + ":\n" + a.getAnswerText());
                }
            }
        });

        Button answerFilterButton = new Button("Filter Answers");
        answerFilterButton.setOnAction(e -> {
            String student = answerFilterField.getText().trim().toLowerCase();
            answersList.setItems(allAnswers.filtered(a -> a.getStudentUserName().toLowerCase().contains(student)));
        });

        answersBox.getChildren().addAll(answersLabel, answerFilterField, answersList, answerFilterButton);
        sections.getChildren().addAll(questionsBox, answersBox);
        qaLayout.getChildren().addAll(header, sections);

        return qaLayout;
    }

    /**
     * Displays a dialog window with detailed information about the selected question.
     *
     * @param question the {@link Question} to show
     */
    private void showQuestionDetails(Question question) {
        Stage dialog = new Stage();
        dialog.setTitle("Question Details");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Title: " + question.getQuestionTitle());
        Label userLabel = new Label("Student: " + question.getStudentUserName());
        Label timeLabel = new Label("Asked on: " + question.getCreationTime());
        Label bodyLabel = new Label("Question:");
        TextArea bodyArea = new TextArea(question.getQuestionBody());
        bodyArea.setWrapText(true);
        bodyArea.setEditable(false);
        bodyArea.setPrefRowCount(6);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> dialog.close());

        layout.getChildren().addAll(titleLabel, userLabel, timeLabel, bodyLabel, bodyArea, closeButton);

        Scene scene = new Scene(layout, 450, 300);
        dialog.setScene(scene);
        dialog.show();
    }

    /**
     * Displays a dialog window with detailed information about the selected answer.
     *
     * @param answer the {@link Answer} to show
     */
    private void showAnswerDetails(Answer answer) {
        Stage dialog = new Stage();
        dialog.setTitle("Answer Details");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label userLabel = new Label("Student: " + answer.getStudentUserName());
        Label timeLabel = new Label("Answered on: " + answer.getCreationTime());
        Label answerLabel = new Label("Answer:");
        TextArea answerArea = new TextArea(answer.getAnswerText());
        answerArea.setWrapText(true);
        answerArea.setEditable(false);
        answerArea.setPrefRowCount(6);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> dialog.close());

        layout.getChildren().addAll(userLabel, timeLabel, answerLabel, answerArea, closeButton);

        Scene scene = new Scene(layout, 450, 300);
        dialog.setScene(scene);
        dialog.show();
    }

    /**
     * Creates and returns a panel for managing reviewer role requests.
     * Instructors can approve or deny requests.
     *
     * @return a {@link Pane} with reviewer request controls
     */
    private Pane createRequestsPane() {
        VBox reqLayout = new VBox(10);
        reqLayout.setPadding(new Insets(10));
        reqLayout.setAlignment(Pos.CENTER);

        Label header = new Label("Reviewer Role Requests");
        header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ObservableList<NewRoleRequest> requests = databaseHelper.getReviewerRequests();
        ListView<NewRoleRequest> requestsList = new ListView<>(requests);

        requestsList.setCellFactory(lv -> new ListCell<NewRoleRequest>() {
            @Override
            protected void updateItem(NewRoleRequest req, boolean empty) {
                super.updateItem(req, empty);
                if (empty || req == null) {
                    setText(null);
                } else {
                    setText("User: " + req.getUserName() + "\nStatus: " + req.getRequestStatus());
                }
            }
        });

        Button approveButton = new Button("Approve");
        approveButton.setOnAction(e -> {
            NewRoleRequest selected = requestsList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                databaseHelper.approveRoleRequest(selected.getRoleRequestID());
                requests.setAll(databaseHelper.getReviewerRequests());
            } else {
                showAlert("Error", "Please select a request to approve.");
            }
        });

        Button denyButton = new Button("Deny");
        denyButton.setOnAction(e -> {
            NewRoleRequest selected = requestsList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                databaseHelper.denyRoleRequest(selected.getRoleRequestID());
                requests.setAll(databaseHelper.getReviewerRequests());
            } else {
                showAlert("Error", "Please select a request to deny.");
            }
        });

        HBox buttons = new HBox(10, approveButton, denyButton);
        buttons.setAlignment(Pos.CENTER);

        reqLayout.getChildren().addAll(header, requestsList, buttons);
        return reqLayout;
    }

    /**
     * Represents a request for a user to be granted a new role,
     * typically reviewer privileges.
     */
    public static class NewRoleRequest {
        private final int roleRequestID;
        private final String userName;
        private final String requestStatus;

        /**
         * Constructs a {@code NewRoleRequest} object.
         *
         * @param roleRequestID the unique ID of the role request
         * @param userName the username of the requesting user
         * @param requestStatus the status of the request
         */
        public NewRoleRequest(int roleRequestID, String userName, String requestStatus) {
            this.roleRequestID = roleRequestID;
            this.userName = userName;
            this.requestStatus = requestStatus;
        }

        /**
         * Gets the ID of the role request.
         *
         * @return the request ID
         */
        public int getRoleRequestID() {
            return roleRequestID;
        }

        /**
         * Gets the username of the user requesting the role.
         *
         * @return the requesting user's username
         */
        public String getUserName() {
            return userName;
        }

        /**
         * Gets the current status of the role request.
         *
         * @return the status as a string
         */
        public String getRequestStatus() {
            return requestStatus;
        }
    }
}
