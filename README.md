Java Login and Registration System

This is my Java programming assignment for a basic user registration and login system, this is the part 1. It runs in the console and asks the user to input their details, checks if everything is formatted correctly, and then lets them log in if the registration was successful.

The code specifically checks for a few things during registration:

Username: Has to be 5 characters or less and MUST contain an underscore (_).
Password: Gotta be at least 8 characters long. It also needs at least one capital letter, one number, and one special character (I used regex to figure this part out).
Phone Number: Must be a valid South African number starting with +27 followed by exactly 9 digits.

Files included:
Main.java: This is where the main program runs. It uses a Scanner to get input from the user and prints out the status messages.

Login.java: This handles all the logic behind the scenes, like checking the passwords, saving the variables, and verifying the login details.

MainTest.java: This has all the JUnit tests to prove the different methods (like strong/weak passwords and valid/invalid usernames) actually work.

How to run it:

Just open the project folder in IntelliJ IDEA (or whatever IDE you use).

To use the app, run the Main.java file and follow the prompts in the terminal.

To check the unit tests, right-click on MainTest.java and hit run. They should all pass and show green ticks!
