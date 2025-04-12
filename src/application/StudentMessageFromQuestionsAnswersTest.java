package application;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import databasePart1.DatabaseHelper;

import java.util.ArrayList;

public class StudentMessageFromQuestionsAnswersTest {

    private DatabaseHelper dbHelper;
    private User testUser;
    private StudentQuestionsAnswers qaPage;

    @BeforeEach
    public void setUp() {
        dbHelper = new DatabaseHelper();
        testUser = new User("testUser", "pass", new boolean[]{false, true, false, false, false}, "test@example.com", "Test", "User");
        qaPage = new StudentQuestionsAnswers(dbHelper);
    }

    @Test
    public void testLoadSubmittedQuestionsAnswers() {
        ArrayList<Question> questions = testUser.getSubmittedQuestions();
        ArrayList<Answer> answers = testUser.getSubmittedAnswers();

        // Expecting no questions or answers yet for a fresh test user
        assertNotNull(questions);
        assertNotNull(answers);
        assertTrue(questions.isEmpty(), "Expected no submitted questions");
        assertTrue(answers.isEmpty(), "Expected no submitted answers");
    }
}