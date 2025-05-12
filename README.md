CSE360 Interactive Peer-Review & Q&A Platform

This project is a JavaFX desktop application designed for students, instructors, reviewers, and administrators to collaborate on coursework through real-time Q&A, peer reviews, and private messaging.

📁 Project Structure

src/
├─ main/
│  ├─ java/com/cse360/
│  │  ├─ controllers/      # JavaFX controller classes for UI pages
│  │  ├─ models/           # Domain classes: User, Question, Answer, Review, etc.
│  │  ├─ utils/            # Validators, recognizers, DatabaseHelper
│  └─ resources/
│     ├─ fxml/             # FXML layout files
│     └─ styles/           # CSS stylesheets
└─ test/
   ├─ java/com/cse360/     # JUnit test classes

🚀 Features

✅ Role-based dashboards for Admin, Instructor, Reviewer, and Student

✅ Real-time Q&A: post questions, submit answers, and comment

✅ Interactive peer-review scorecard for structured feedback

✅ Secure, in-app private messaging with unread indicators

✅ Account setup wizard with email, username, and password validation

✅ Automatic activity logging and summary report generation

🛠️ Technologies

Java 17+

JavaFX 17+

Maven for build and dependency management

JUnit 5 for unit testing

SQLite or MySQL (configurable via DatabaseHelper)

🧪 Running the Project

✅ In Your IDE

Clone or download this repository.

Import as a Maven project (File → Import → Existing Maven Project).

Run StartCSE360.java as a JavaFX Application.

✅ To Run Tests

Right-click on the src/test/java folder → Run As → JUnit Test.

📄 Author

Sajjad "Sayan" SheykhiB.S. Computer Science @ ASU

💬 Notes

Ensure your database is set up and the JDBC URL is configured in DatabaseHelper.java.

The UI is designed for academic demonstration and can be extended for production use.

Contributions, issues, and suggestions are welcome!

