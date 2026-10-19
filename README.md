Java Login, Registration and Messaging System

This is my Java programming assignment for a basic user registration and login system (part 1) combined with the messaging and chat features for part 2. It runs in the console and asks the user to input their details, checks if everything is formatted correctly, lets them log in if the registration was successful, and gives access to the QuickChat menu.

The code specifically checks for a few things during registration:

Username: Has to be 5 characters or less and MUST contain an underscore (_). Password: Gotta be at least 8 characters long. It also needs at least one capital letter, one number, and one special character (I used regex to figure this part out). Phone Number: Must be a valid South African number starting with +27 followed by exactly 9 digits.

For Part 2, once logged in, the app displays a numeric menu for QuickChat, automatically generates 10-digit Message IDs and uppercase Message Hashes, checks that messages are under 250 characters, and allows users to send, discard, or store messages in a JSON file.

Files included:

Main.java: This is where the main program runs. It uses a Scanner to get input from the user, runs the main application loop, and prints out the status messages.

Login.java: This handles all the logic behind the scenes, like checking the passwords, saving the variables, and verifying the login details.

Message.java: This handles all the Part 2 messaging logic, including ID generation, hash creation, recipient validation, and message actions like sending, storing to JSON, or discarding.

MainTest.java: This has all the JUnit tests to prove the different methods (like strong/weak passwords, valid/invalid usernames, and Part 2 message validations) actually work.

MessageTest.java: This has all the JUnit tests for Part 2 to verify message length limits, recipient cell formatting, message hashes, and send/store/discard actions.

How to run it:

Just open the project folder in IntelliJ IDEA (or whatever IDE you use).

To use the app, run the Main.java file and follow the prompts in the terminal.

To check the unit tests, right click on MainTest.java and hit run. They should all pass and show green ticks. 
