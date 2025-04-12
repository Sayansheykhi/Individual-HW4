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

import java.util.ArrayList;

/**
 * The {@code StaffQuestionsAnswersPage} class allows staff to:
 * 1) View & filter questions (plus inline answers),
 * 2) Search by keyword & status (All, Resolved, Unresolved),
 * 3) Moderate (edit or delete) the selected question.
 *
 * <p>Author: Sajjad</p>
 */
public class StaffQuestionsAnswersPage {

    private DatabaseHelper databaseHelper;
    private User staffUser;

    // We store questions in an ObservableList so we can easily filter & refresh
    private ObservableList<Question> questionsObservable;

    // We keep a reference to all answers so we can display them inline
    private ArrayList<Answer> allAnswers;

    // A ListView for the question + answer display
    private ListView<Question> questionsListView;

    // We also keep a FilteredList for dynamic searching
    private FilteredList<Question> filteredData;

    /**
     * Constructor for the StaffQuestionsAnswersPage.
     * @param db        The database helper
     * @param staffUser The logged-in staff user
     */
    public StaffQuestionsAnswersPage(DatabaseHelper db, User staffUser) {
        this.databaseHelper = db;
        this.staffUser = staffUser;
    }

    /**
     * Displays this page in the given primaryStage.
     * It shows all questions & answers, plus a search/filter,
     * and a 'Moderate' button to edit/delete the selected question.
     */
    public void show(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_LEFT);

        // Title
        Label titleLabel = new Label("All Submitted Questions and Answers (Staff)");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        root.getChildren().add(titleLabel);

        // Filter controls
        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("Search keyword...");

        ChoiceBox<String> statusChoice = new ChoiceBox<>();
        statusChoice.getItems().addAll("All", "Resolved", "Unresolved");
        statusChoice.setValue("All"); // default filter is 'All'

        filterBox.getChildren().addAll(
            new Label("Search:"),
            searchField,
            new Label("Status:"),
            statusChoice
        );
        root.getChildren().add(filterBox);

        // Our ListView for Q + A
        questionsListView = new ListView<>();
        questionsListView.setPrefHeight(400);

        // Prepare data in the "refreshData()" method
        // so we can call it again after editing/deleting
        refreshData();

        // Now we have "questionsObservable" and "allAnswers" loaded.
        // We'll wrap it in a FilteredList:
        filteredData = new FilteredList<>(questionsObservable, p -> true);
        questionsListView.setItems(filteredData);

        // Whenever user types or changes the status, update the filter
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            updateFilter(searchField.getText(), statusChoice.getValue());
        });
        statusChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updateFilter(searchField.getText(), statusChoice.getValue());
        });

        // Custom cell factory to display question + inline answers
        questionsListView.setCellFactory(lv -> new ListCell<Question>() {
            @Override
            protected void updateItem(Question q, boolean empty) {
                super.updateItem(q, empty);
                if (empty || q == null) {
                    setText(null);
                } else {
                    String qInfo = "QID: " + q.getQuestionID()
                            + "  Title: " + q.getQuestionTitle()
                            + "  By: " + q.getStudentUserName()
                            + (q.getIsResolved() ? " [Resolved]" : " [Unresolved]");

                    StringBuilder sb = new StringBuilder(qInfo);
                    // Show inline answers
                    for (Answer a : allAnswers) {
                        if (a.getQuestionID() == q.getQuestionID()) {
                            sb.append("\n    ↳ A: ").append(a.getAnswerText())
                              .append(" (by ").append(a.getStudentUserName()).append(")");
                            if (a.getIsResolved()) {
                                sb.append(" [Answer Resolved]");
                            }
                        }
                    }
                    setText(sb.toString());
                }
            }
        });

        root.getChildren().add(questionsListView);

        // A 'Moderate' button to edit or delete the selected question
        Button moderateButton = new Button("Moderate Selected Q");
        moderateButton.setOnAction(e -> moderateSelectedQuestion());
        root.getChildren().add(moderateButton);

        // A back button to staff home
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            // Return to staff home
            StaffHomePage staffHome = new StaffHomePage(databaseHelper, staffUser);
            staffHome.show(primaryStage, staffUser);
        });
        root.getChildren().add(backButton);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Staff: Q&A Search/Filter + Moderation");
        primaryStage.show();
    }

    /**
     * Refreshes 'questionsObservable' and 'allAnswers' from the DB,
     * so if we edit/delete something, we can call this again to update the UI.
     */
    private void refreshData() {
        ArrayList<Question> questions = databaseHelper.getAllQuestions(staffUser);
        allAnswers = databaseHelper.getAllAnswers(staffUser);
        if (questionsObservable == null) {
            questionsObservable = FXCollections.observableArrayList(questions);
        } else {
            questionsObservable.setAll(questions);
        }
    }

    /**
     * Updates the FilteredList predicate to match the given keyword + status.
     */
    private void updateFilter(String keyword, String status) {
        if (filteredData == null) return; // defensive check

        filteredData.setPredicate(question -> {
            // If everything is blank, show all
            if ((keyword == null || keyword.isEmpty()) && status.equals("All")) {
                return true;
            }
            // 1) Check keyword
            boolean matchesKeyword = true;
            if (keyword != null && !keyword.isEmpty()) {
                String lower = keyword.toLowerCase();
                String combined = (question.getQuestionTitle() + " "
                                 + question.getQuestionBody() + " "
                                 + question.getStudentUserName()).toLowerCase();
                matchesKeyword = combined.contains(lower);
            }

            // 2) Check status
            boolean matchesStatus = true;
            if (!status.equals("All")) {
                boolean resolved = question.getIsResolved();
                if (status.equals("Resolved") && !resolved) {
                    matchesStatus = false;
                } else if (status.equals("Unresolved") && resolved) {
                    matchesStatus = false;
                }
            }
            return matchesKeyword && matchesStatus;
        });
    }

    /**
     * Allows staff to moderate (edit/delete) the currently selected question in the ListView.
     */
    private void moderateSelectedQuestion() {
        Question selectedQ = questionsListView.getSelectionModel().getSelectedItem();
        if (selectedQ == null) {
            showAlert("No Selection", "Please select a question to moderate.");
            return;
        }

        // We'll let staff choose: Delete or Edit
        Alert moderateAlert = new Alert(Alert.AlertType.CONFIRMATION);
        moderateAlert.setTitle("Moderate Question");
        moderateAlert.setHeaderText("Question: " + selectedQ.getQuestionTitle());
        moderateAlert.setContentText("Choose an action: Delete or Edit?");

        ButtonType deleteBtn = new ButtonType("Delete");
        ButtonType editBtn   = new ButtonType("Edit");
        ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        moderateAlert.getButtonTypes().setAll(deleteBtn, editBtn, cancelBtn);

        moderateAlert.showAndWait().ifPresent(response -> {
            if (response == deleteBtn) {
                // Confirm staff has permission, if needed
                // if (!staffUser.hasModerationPermission()) { ... }
                databaseHelper.deleteQuestion(selectedQ.getQuestionID());
                showAlert("Deleted", "Question has been removed.");
                refreshData();

            } else if (response == editBtn) {
                // Let staff provide new text for the questionBody
                TextInputDialog dialog = new TextInputDialog(selectedQ.getQuestionBody());
                dialog.setTitle("Edit Question Body");
                dialog.setHeaderText("Edit the question content");
                dialog.setContentText("New text:");

                dialog.showAndWait().ifPresent(newBody -> {
                    // If you want to let them edit the title as well, you can do another prompt,
                    // or just keep the same title for now:
                    String oldTitle = selectedQ.getQuestionTitle();
                    databaseHelper.editQuestion(oldTitle, newBody, selectedQ.getQuestionID());

                    showAlert("Updated", "Question text updated.");
                    refreshData();
                });
            }
        });
    }

    /**
     * Helper to show an alert box with a message.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
