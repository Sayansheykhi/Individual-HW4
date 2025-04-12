package application;

import static org.junit.jupiter.api.Assertions.*;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import org.junit.jupiter.api.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * A JUnit test class for StaffQuestionsAnswersPage that uses a simple
 * FakeDatabaseHelper (no mocks) and reflection to test private methods/fields.
 *
 * <p>No Mockito used. We rely on a manual stub approach to supply data
 * and to confirm logic in StaffQuestionsAnswersPage.</p>
 * 
 * @author sajjad
 */
class StaffQuestionsAnswersPageTest {

    private FakeDatabaseHelper fakeDbHelper;
    private User fakeStaffUser;
    private StaffQuestionsAnswersPage pageUnderTest;

    /**
     * A minimal "fake" DatabaseHelper that doesn't hit a real DB,
     * just returns in-memory data for testing.
     */
    static class FakeDatabaseHelper extends DatabaseHelper {
        private ArrayList<Question> questionsData;
        private ArrayList<Answer> answersData;

        public FakeDatabaseHelper(ArrayList<Question> qData, ArrayList<Answer> aData) {
            this.questionsData = qData;
            this.answersData = aData;
        }

        @Override
        public ArrayList<Question> getAllQuestions(User user) {
            // We ignore 'user' for staff, just return the array.
            return questionsData;
        }

        @Override
        public ArrayList<Answer> getAllAnswers(User user) {
            return answersData;
        }

        // If you want stubs for deleteQuestion(...) or editQuestion(...),
        // you can override those too, e.g.:

        @Override
        public void deleteQuestion(int questionID) {
            // Remove from 'questionsData'
            questionsData.removeIf(q -> q.getQuestionID() == questionID);
        }

        @Override
        public void editQuestion(String title, String newBody, int questionID) {
            // Find the question, update the body
            for (Question q : questionsData) {
                if (q.getQuestionID() == questionID) {
                    q.setQuestionBody(newBody);
                    // if you want to store title, do that too
                    break;
                }
            }
        }

        // etc. if you want answers edited or deleted
    }

    @BeforeEach
    void setUp() {
        // Create some test data
        ArrayList<Question> qData = new ArrayList<>();
        qData.add(new Question(101, "userA", "Afirst", "Alast", "TitleA", "BodyA", false, null));
        qData.add(new Question(102, "userB", "Bfirst", "Blast", "TitleB", "BodyB", true, null));

        ArrayList<Answer> aData = new ArrayList<>();
        aData.add(new Answer(201, 101, "userA", "Afirst", "Alast", "Answer for Q101", false, false, null));
        aData.add(new Answer(202, 102, "userC", "Cfirst", "Clast", "Answer for Q102", false, false, null));

        fakeDbHelper = new FakeDatabaseHelper(qData, aData);
        fakeStaffUser = new User("staffUser", "staffPass", new boolean[]{false,false,false,false,true},
                                 "staff@domain.com", "StaffF", "StaffL");

        pageUnderTest = new StaffQuestionsAnswersPage(fakeDbHelper, fakeStaffUser);
    }

    @Test
    void testRefreshDataViaReflection() throws Exception {
        // Use reflection to call private refreshData()
        Method refreshMethod = StaffQuestionsAnswersPage.class.getDeclaredMethod("refreshData");
        refreshMethod.setAccessible(true);
        refreshMethod.invoke(pageUnderTest);

        // Then reflect on questionsObservable
        Field fieldQObs = StaffQuestionsAnswersPage.class.getDeclaredField("questionsObservable");
        fieldQObs.setAccessible(true);

        @SuppressWarnings("unchecked")
        javafx.collections.ObservableList<Question> qObs =
            (javafx.collections.ObservableList<Question>) fieldQObs.get(pageUnderTest);

        assertEquals(2, qObs.size(), "Should have 2 questions loaded from the fake DB");
        assertEquals(101, qObs.get(0).getQuestionID());
        assertEquals(102, qObs.get(1).getQuestionID());
    }

    @Test
    void testUpdateFilterViaReflection() throws Exception {
        // We'll call refreshData first to ensure questionsObservable is set
        Method refreshMethod = StaffQuestionsAnswersPage.class.getDeclaredMethod("refreshData");
        refreshMethod.setAccessible(true);
        refreshMethod.invoke(pageUnderTest);

        // Then get questionsObservable and filteredData
        Field fieldQObs = StaffQuestionsAnswersPage.class.getDeclaredField("questionsObservable");
        fieldQObs.setAccessible(true);

        Field fieldFData = StaffQuestionsAnswersPage.class.getDeclaredField("filteredData");
        fieldFData.setAccessible(true);

        @SuppressWarnings("unchecked")
        javafx.collections.ObservableList<Question> qObs =
            (javafx.collections.ObservableList<Question>) fieldQObs.get(pageUnderTest);
        // Now we create a FilteredList from it
        FilteredList<Question> fData = new FilteredList<>(qObs, p -> true);
        fieldFData.set(pageUnderTest, fData);

        // Next, reflect on updateFilter(String,String)
        Method updateFilterMethod = StaffQuestionsAnswersPage.class.getDeclaredMethod("updateFilter", String.class, String.class);
        updateFilterMethod.setAccessible(true);

        // (A) Filter with "TitleA" + "All"
        updateFilterMethod.invoke(pageUnderTest, "TitleA", "All");
        assertEquals(1, fData.size(), "Should only match question with TitleA");
        assertEquals(101, fData.get(0).getQuestionID());

        // (B) Filter with "" + "Resolved"
        updateFilterMethod.invoke(pageUnderTest, "", "Resolved");
        assertEquals(1, fData.size(), "Should only match the resolved question (ID=102)");
        assertEquals(102, fData.get(0).getQuestionID());

        // (C) Filter with "BodyB" + "Unresolved"
        updateFilterMethod.invoke(pageUnderTest, "BodyB", "Unresolved");
        // Q102 is resolved => no match
        assertEquals(0, fData.size(), "No unresolved question containing BodyB");
    }

    @Test
    void testModerateSelectedQuestionPartial() throws Exception {
        // We'll just confirm the fakeDbHelper's overridden methods work:
        // 1) refreshData => have questions 101 and 102
        Method refreshMethod = StaffQuestionsAnswersPage.class.getDeclaredMethod("refreshData");
        refreshMethod.setAccessible(true);
        refreshMethod.invoke(pageUnderTest);

        // 2) call "deleteQuestion(101)" in the fake DB
        fakeDbHelper.deleteQuestion(101);

        // The question array should now have 1 question left
        ArrayList<Question> newQs = fakeDbHelper.getAllQuestions(fakeStaffUser);
        assertEquals(1, newQs.size(), "Should have removed Q ID=101");
        assertEquals(102, newQs.get(0).getQuestionID());

        // 3) call "editQuestion" on question 102
        fakeDbHelper.editQuestion("TitleB", "NewBody", 102);
        // Check it changed
        assertEquals("NewBody", newQs.get(0).getQuestionBody());
    }
}
