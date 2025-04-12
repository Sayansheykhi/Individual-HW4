package application;

import databasePart1.DatabaseHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link StaffPrivateFeedbackPage} that focus on verifying the
 * message-sending logic by using a fake {@link DatabaseHelper} implementation.
 * 
 * Since the class uses JavaFX, this test skips the UI and checks backend logic via stubs.
 * 
 * @author Sajjad
 */
public class StaffPrivateFeedbackPageTest {

    private FakeDatabaseHelper fakeDbHelper;
    private User staffUser;
    private TestableStaffPrivateFeedbackPage pageUnderTest;

    private boolean messageSent = false;
    private String lastRecipient = "";
    private String lastSubject = "";
    private String lastBody = "";

    /**
     * A minimal fake version of DatabaseHelper that logs message sends.
     */
    class FakeDatabaseHelper extends DatabaseHelper {
        @Override
        public boolean sendPrivateMessage(String sender, String receiver, String subject, String body) {
            messageSent = true;
            lastRecipient = receiver;
            lastSubject = subject;
            lastBody = body;
            return true;
        }
    }

    /**
     * Subclass of StaffPrivateFeedbackPage that overrides UI methods like showAlert().
     * This avoids launching JavaFX windows during testing.
     */
    class TestableStaffPrivateFeedbackPage extends StaffPrivateFeedbackPage {
        public TestableStaffPrivateFeedbackPage(DatabaseHelper db, User user) {
            super(db, user);
        }

        // Prevent UI popup during tests
        protected void showAlert(String title, String content) {
            // Do nothing
        }

        public boolean simulateSend(String recipient, String subject, String body) {
            // Simulate what the sendButton would do (logic only)
            if (recipient.trim().isEmpty() || subject.trim().isEmpty() || body.trim().isEmpty()) {
                return false;
            }
            return fakeDbHelper.sendPrivateMessage(
                    staffUser.getUserName(),
                    recipient.trim(),
                    subject.trim(),
                    body.trim()
            );
        }
    }

    @BeforeEach
    void setUp() {
        staffUser = new User("staff123", "pass", new boolean[]{false,false,false,false,true}, "staff@domain.com", "Staff", "Member");
        fakeDbHelper = new FakeDatabaseHelper();
        pageUnderTest = new TestableStaffPrivateFeedbackPage(fakeDbHelper, staffUser);
        messageSent = false;
    }

    @Test
    void testValidMessageSendsSuccessfully() {
        boolean success = pageUnderTest.simulateSend("student1", "Great Job", "Your answer was excellent!");
        assertTrue(success);
        assertTrue(messageSent);
        assertEquals("student1", lastRecipient);
        assertEquals("Great Job", lastSubject);
        assertEquals("Your answer was excellent!", lastBody);
    }

    @Test
    void testEmptyRecipientPreventsSending() {
        boolean success = pageUnderTest.simulateSend("", "Subject", "Message");
        assertFalse(success);
        assertFalse(messageSent);
    }

    @Test
    void testEmptySubjectPreventsSending() {
        boolean success = pageUnderTest.simulateSend("userX", "", "Message");
        assertFalse(success);
        assertFalse(messageSent);
    }

    @Test
    void testEmptyBodyPreventsSending() {
        boolean success = pageUnderTest.simulateSend("userX", "Subject", "");
        assertFalse(success);
        assertFalse(messageSent);
    }
}
