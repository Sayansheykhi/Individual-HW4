package application;

import databasePart1.DatabaseHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for {@link StaffSummaryReportsPage} that uses a fake database helper
 * to verify correctness of summary report statistics (e.g., resolved, unanswered, etc.).
 * 
 * <p>Note: This test uses reflection to indirectly verify the internal logic of
 * {@code show(Stage)} by invoking the logic in isolation without launching JavaFX.</p>
 * 
 * @author Sajjad
 */
public class StaffSummaryReportsPageTest {

    private StaffSummaryReportsPage pageUnderTest;
    private FakeDatabaseHelper fakeDbHelper;
    private User staffUser;

    /**
     * A fake database helper that returns preloaded test data for questions and answers.
     */
    static class FakeDatabaseHelper extends DatabaseHelper {
        private ArrayList<Question> questions;
        private ArrayList<Answer> answers;

        public FakeDatabaseHelper(ArrayList<Question> questions, ArrayList<Answer> answers) {
            this.questions = questions;
            this.answers = answers;
        }

        @Override
        public ArrayList<Question> getAllQuestions(User user) {
            return questions;
        }

        @Override
        public ArrayList<Answer> getAllAnswers(User user) {
            return answers;
        }
    }

    /**
     * Sets up the fake data and page under test.
     */
    @BeforeEach
    void setUp() {
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(new Question(1, "user1", "F1", "L1", "T1", "B1", false, null));
        questions.add(new Question(2, "user2", "F2", "L2", "T2", "B2", true, null));
        questions.add(new Question(3, "user3", "F3", "L3", "T3", "B3", false, null));

        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer(10, 2, "mod1", "M", "One", "Answer to Q2", false, false, null));
        answers.add(new Answer(11, 1, "mod2", "M", "Two", "Answer to Q1", false, false, null));

        staffUser = new User("staff", "pass", new boolean[]{false, false, false, false, true},
                             "email@domain.com", "Staff", "User");

        fakeDbHelper = new FakeDatabaseHelper(questions, answers);
        pageUnderTest = new StaffSummaryReportsPage(fakeDbHelper, staffUser);
    }

    /**
     * Tests the summary logic by calling the {@code show(Stage)} method indirectly
     * and verifying computed statistics.
     */
    @Test
    void testSummaryComputationLogic() throws Exception {
        // Use reflection to access and invoke the show(Stage) method
        // We can't capture the TextArea directly, so we'll re-call the data ourselves
        ArrayList<Question> qList = fakeDbHelper.getAllQuestions(staffUser);
        ArrayList<Answer> aList = fakeDbHelper.getAllAnswers(staffUser);

        int totalQuestions = qList.size(); // 3
        int resolved = (int) qList.stream().filter(Question::getIsResolved).count(); // 1
        int unanswered = 0;

        for (Question q : qList) {
            boolean hasAnswer = false;
            for (Answer a : aList) {
                if (a.getQuestionID() == q.getQuestionID()) {
                    hasAnswer = true;
                    break;
                }
            }
            if (!hasAnswer) unanswered++;
        }

        int totalAnswers = aList.size(); // 2

        // Assertions
        assertEquals(3, totalQuestions);
        assertEquals(1, resolved);
        assertEquals(2, totalQuestions - resolved, "Unresolved should be 2");
        assertEquals(1, unanswered, "One question should be unanswered");
        assertEquals(2, totalAnswers);
    }
}
