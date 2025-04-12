package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;

/**
 * The {@code StaffSummaryReportsPage} class provides functionality for staff users
 * to view summary reports on Q&A activity, including total questions, resolved,
 * unresolved, and unanswered counts, as well as total answers.
 *
 * <p>This page is displayed by calling the {@link #show(Stage)} method, which
 * builds a simple UI with a text area to display the computed stats and
 * a Back button to return the user to the {@code StaffHomePage}.</p>
 *
 * @author Sajjad
 */
public class StaffSummaryReportsPage {

    /** A reference to the {@link DatabaseHelper} for database operations. */
    private final DatabaseHelper databaseHelper;

    /** The currently logged-in staff user. */
    private final User staffUser;

    /**
     * Constructs a {@code StaffSummaryReportsPage} with the specified database helper
     * and the staff user who is currently logged in.
     *
     * @param db   the {@link DatabaseHelper} used to fetch questions/answers
     * @param user the staff {@link User} instance representing the logged-in user
     */
    public StaffSummaryReportsPage(DatabaseHelper db, User user) {
        this.databaseHelper = db;
        this.staffUser = user;
    }

    /**
     * Displays the summary reports page in the provided {@link Stage}.
     * <p>
     * This method retrieves all questions and answers from the database, calculates
     * basic statistics (such as the number of total questions, answered vs. unanswered,
     * and resolved vs. unresolved), and displays the results in a read-only text area.
     * A Back button is provided to return to the {@code StaffHomePage}.
     * </p>
     *
     * @param primaryStage the primary {@link Stage} on which to display this scene
     */
    public void show(Stage primaryStage) {
        // 1) Main layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_LEFT);

        // 2) Title label
        Label titleLabel = new Label("Q&A Summary Reports");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        root.getChildren().add(titleLabel);

        // 3) A read-only TextArea to show the stats
        TextArea reportArea = new TextArea();
        reportArea.setEditable(false);
        reportArea.setPrefHeight(300);

        // 4) Retrieve data from DB (assuming these methods exist in DatabaseHelper)
        ArrayList<Question> allQuestions = databaseHelper.getAllQuestions(staffUser);
        ArrayList<Answer>   allAnswers   = databaseHelper.getAllAnswers(staffUser);

        // 5) Compute basic stats
        int totalQuestions   = allQuestions.size();
        int resolvedCount    = 0;
        int unansweredCount  = 0;  // for questions with zero answers

        // Count how many are resolved
        for (Question q : allQuestions) {
            if (q.getIsResolved()) {
                resolvedCount++;
            }
        }

        // Count how many have no answers
        for (Question q : allQuestions) {
            boolean hasAnswer = false;
            for (Answer a : allAnswers) {
                if (a.getQuestionID() == q.getQuestionID()) {
                    hasAnswer = true;
                    break;
                }
            }
            if (!hasAnswer) {
                unansweredCount++;
            }
        }

        int totalAnswers = allAnswers.size();

        // 6) Build the summary string
        StringBuilder sb = new StringBuilder();
        sb.append("Total Questions: ").append(totalQuestions).append("\n");
        sb.append("Resolved: ").append(resolvedCount).append("\n");
        sb.append("Unresolved: ").append(totalQuestions - resolvedCount).append("\n");
        sb.append("Unanswered: ").append(unansweredCount).append("\n\n");
        sb.append("Total Answers: ").append(totalAnswers).append("\n");

        // 7) Show result in the text area
        reportArea.setText(sb.toString());
        root.getChildren().add(reportArea);

        // 8) A back button to return staff to the StaffHomePage
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            StaffHomePage staffHome = new StaffHomePage(databaseHelper, staffUser);
            staffHome.show(primaryStage, staffUser);
        });
        root.getChildren().add(backButton);

        // 9) Build scene
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Staff: Summary Reports");
        primaryStage.show();
    }
}
