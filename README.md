Java Login, Registration and QuickChat Messaging System

This project is my Java programming assignment for PROG5121 at Rosebank College, Cape Town. What started as a basic user registration and login system in Part 1 has grown into a console-based messaging application called QuickChat.

I'm proud of the effort I've put into this project and how much I've learned along the way. It hasn't just been about writing code. I've had to solve problems, fix errors, test my work, pay attention to detail, and keep going when something didn't work the first time. Each part has helped me become more confident in my programming skills and better at thinking through problems independently.

Part 1: Registration and Login

The first part of the project focused on creating a registration and login system. The application runs in the console and asks users to enter their details. It checks whether the details meet the required rules before allowing the user to log in.

The code checks the following:

Username: Must be 5 characters or less and contain an underscore (_).

Password: Must be at least 8 characters long and contain at least one uppercase letter, one number, and one special character. I used regular expressions (regex) to help validate the password.

Phone Number: Must follow the required international format for a South African number, starting with +27 followed by exactly 9 digits.

The project also includes login validation to check whether the user's login details match the registered details.

Part 2: QuickChat Messaging

In Part 2, I developed the project further by introducing messaging functionality. This helped me understand how separate classes can work together to create a more complete application.

The application includes features such as:

A welcome message and a menu that allows the user to choose an action.

Sending messages to recipients.

Generating message IDs and keeping track of message numbers.

Creating message hashes using the required format.

Checking message length and recipient details.

Giving the user the option to send, store, or disregard a message.

Displaying message details.

Saving stored messages in JSON format.

Using JUnit tests to check that different parts of the program work as expected.

Part 3: Message Management and Testing

Part 3 builds on the earlier work by focusing on managing messages and making the application more complete.

The required functionality includes working with message arrays, viewing stored messages, searching for messages by ID or recipient, finding the longest message, deleting a stored message using its hash, and displaying a report of message information.

I am also working on improving the reliability of the application through testing, checking that the features work together correctly, and preparing the project for automated testing through GitHub Actions.

Files Included

Main.java: Runs the application, displays the menu, collects user input, and manages the main program flow.

Login.java: Handles user details, registration validation, and login verification.

Message.java: Handles message-related functionality, including message validation, IDs, hashes, and message information.

MainTest.java: Contains JUnit tests for the registration and login functionality.

MessageTest.java: Contains JUnit tests for the message functionality.

Other project files may include the JSON storage file, build configuration, additional test classes, and GitHub Actions workflow files as the project develops.

How to Run It

Open the project in IntelliJ IDEA.

Make sure the Java SDK and any required dependencies are configured.

Run Main.java.

Follow the instructions displayed in the console to register, log in, and use the available QuickChat features.

To run the tests, open the relevant test class, such as MainTest.java or MessageTest.java, and select the Run option in IntelliJ.

The tests should pass if the code and test environment are correctly configured.

What I Have Learned:

One of the biggest lessons from this project is that programming involves much more than getting code to compile. I have learned the importance of testing, debugging, following requirements, organising code into classes, and making sure different parts of an application work together.

I have also developed patience, persistence, attention to detail, and problem-solving skills. When I encountered errors, I learned to investigate them, make changes, and test the results instead of giving up or starting over unnecessarily.

Working through the different parts has also taught me the importance of planning, maintaining readable code, and checking my work against the project requirements. Using IntelliJ IDEA and JUnit has helped me become more comfortable with development tools and testing practices.

I am especially proud of the progress from the original registration and login system to the more advanced QuickChat application. This project has helped me build a stronger foundation in Java and has motivated me to keep learning and improving.

GitHub and Continuous Improvement

I am using GitHub to manage and present my project as part of my development journey. I am working on maintaining a clear project history, recording changes through commits, and setting up automated testing so that the application can be checked more consistently.

My goal is to produce a project that is not only functional but also organised, tested, understandable, and easy for another person to review.

Final Thoughts:

QuickChat represents an important step in my journey as a developing programmer. I know there is always more to learn, but I am proud of what I have accomplished so far and the effort I have put into improving my work.

Beyond the technical skills, this assignment has helped me develop a more responsible approach to learning, problem-solving, and completing a project. I hope to continue building on these skills in future projects and eventually apply them to real-world problems.

Thank you for taking the time to look at my work.
