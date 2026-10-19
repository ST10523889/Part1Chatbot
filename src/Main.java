import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Registration variables
        String firstName;
        String lastName;
        String username;
        String password;
        String cellPhoneNumber;

        // Registration
        System.out.println("Registration");

        System.out.println("Enter your first name:");
        firstName = scanner.nextLine();

        System.out.println("Enter your last name:");
        lastName = scanner.nextLine();

        System.out.println("Enter your username:");
        username = scanner.nextLine();

        System.out.println("Enter your password:");
        password = scanner.nextLine();

        System.out.println("Enter your phone number:");
        cellPhoneNumber = scanner.nextLine();

        // Create user
        Login user = new Login(
                username,
                password,
                cellPhoneNumber,
                firstName,
                lastName
        );

        // Register user
        String registrationResult = user.registerUser();

        System.out.println("\n--- Registration Status ---");
        System.out.println(registrationResult);

        // Only allow login if registration succeeded
        if (user.checkUserName()
                && user.checkPasswordComplexity()
                && user.checkCellPhoneNumber()) {

            System.out.println("\nLogin user");

            System.out.println("Enter username for login:");
            String inputUsername = scanner.nextLine();

            System.out.println("Enter password for login:");
            String inputPassword = scanner.nextLine();

            // Authenticate
            boolean isAuthentication =
                    user.loginUser(
                            inputUsername,
                            inputPassword
                    );

            String loginOutput =
                    user.returnLoginStatus(isAuthentication);

            System.out.println("\n--- Login Status ---");
            System.out.println(loginOutput);

            // Only users that are logged in can use QuickChat
            if (isAuthentication) {
                runQuickChat(scanner);
            }

        } else {

            System.out.println(
                    "\n[!] Registration failed, please try again."
            );
        }
    }

    // QuickChat application
    private static void runQuickChat(Scanner scanner) {

        // Requirement 2
        System.out.println("\nWelcome to QuickChat.");

        // User chooses the number of messages to send
        int numberOfMessages =
                readPositiveInteger(
                        scanner,
                        "Enter the number of messages you would like to enter: "
                );

        int messagesProcessed = 0;

        // Requirement 4 - while loop keeps the program running
        while (true) {

            System.out.println("\n--- QuickChat Menu ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");

            System.out.print("Choose an option: ");

            String menuChoice = scanner.nextLine();

            switch (menuChoice) {

                case "1":

                    // Make sure user cannot exceed their selected amount
                    if (messagesProcessed >= numberOfMessages) {

                        System.out.println(
                                "You have reached the number of messages you selected."
                        );

                        System.out.println(
                                "Total messages sent: "
                                        + getTotalMessages()
                        );

                        break;
                    }

                    // Requirement 5 - for loop to control the number of messages
                    for (int messageNumber = messagesProcessed;
                         messageNumber < numberOfMessages;
                         messageNumber++) {

                        Message newMessage =
                                createMessage(
                                        scanner,
                                        messageNumber
                                );

                        System.out.println(
                                "\n"
                                        + newMessage.messageLengthStatus()
                        );

                        // Display hash
                        System.out.println(
                                "\nMessage Hash: "
                                        + newMessage.createMessageHash()
                        );

                        // Choose to either Send, Disregard, or Store
                        int action =
                                readMessageAction(scanner, newMessage);

                        String actionResult =
                                newMessage.SentMessage(action);

                        System.out.println(actionResult);

                        // Disregard the message
                        if (action == 2) {

                            System.out.print(
                                    "Enter 0 to delete the message: "
                            );

                            String deleteChoice =
                                    scanner.nextLine();

                            if (deleteChoice.equals("0")) {

                                System.out.println(
                                        "Message deleted."
                                );
                            }
                        }

                        // Display the complete details after sending the message
                        if (action == 1) {

                            System.out.println(
                                    "\n--- Message Details ---"
                            );

                            System.out.println(
                                    "Message ID: "
                                            + newMessage.getMessageID()
                            );

                            System.out.println(
                                    "Message Hash: "
                                            + newMessage.createMessageHash()
                            );

                            System.out.println(
                                    "Recipient: "
                                            + newMessage.getRecipientCell()
                            );

                            System.out.println(
                                    "Message: "
                                            + newMessage.getMessage()
                            );
                        }

                        messagesProcessed++;
                    }

                    // Display the total amount of messages sent
                    System.out.println(
                            "\nTotal messages sent: "
                                    + getTotalMessages()
                    );

                    break;

                case "2":

                    // 3(b)
                    System.out.println("Coming Soon.");
                    break;

                case "3":

                    // 3(c)
                    System.out.println(
                            "Thank you for using QuickChat."
                    );

                    return;

                default:

                    System.out.println(
                            "Invalid option. Please select 1, 2, or 3."
                    );
            }
        }
    }

    // Creates a message
    private static Message createMessage(
            Scanner scanner,
            int messageNumber) {

        String recipient;

        // Validates the recipient
        while (true) {

            System.out.print(
                    "Enter recipient cell number: "
            );

            recipient = scanner.nextLine();

            Message recipientCheck =
                    new Message(
                            "Temporary message",
                            recipient,
                            messageNumber
                    );

            String recipientStatus =
                    recipientCheck.checkRecipientCell();

            System.out.println(recipientStatus);

            if (recipientStatus.equals(
                    "Cell phone number successfully captured."
            )) {

                break;
            }
        }

        String messageText;

        // Validates the message length
        while (true) {

            System.out.print(
                    "Enter your message: "
            );

            messageText = scanner.nextLine();

            Message messageCheck =
                    new Message(
                            messageText,
                            recipient,
                            messageNumber
                    );

            if (messageCheck.checkMessageLength()) {

                break;
            }

            System.out.println(
                    "Please enter a message of less than 250 characters."
            );

            System.out.println(
                    messageCheck.messageLengthStatus()
            );
        }

        // Creates final message object
        return new Message(
                messageText,
                recipient,
                messageNumber
        );
    }

    // Allows user to choose what happens to the message
    private static int readMessageAction(
            Scanner scanner, Message message) {

        while (true) {

            System.out.println("\nChoose what to do with this message:");
            System.out.println(message.SentMessage());

            System.out.print("Choose an option: ");
            String actionInput = scanner.nextLine();

            if (actionInput.equals("1")
                    || actionInput.equals("2")
                    || actionInput.equals("3")) {

                return Integer.parseInt(actionInput);
            }

            System.out.println(
                    "Invalid option. Please select 1, 2, or 3."
            );
        }
    }

    // Reads a positive integer
    private static int readPositiveInteger(
            Scanner scanner,
            String prompt) {

        while (true) {

            System.out.print(prompt);

            String input = scanner.nextLine();

            try {

                int number =
                        Integer.parseInt(input);

                if (number > 0) {
                    return number;
                }

            } catch (NumberFormatException e) {

                // Keep asking for valid input
            }

            System.out.println(
                    "Please enter a number greater than 0."
            );
        }
    }

    // Gets the total sent messages
    private static int getTotalMessages() {

        Message totalMessageHelper =
                new Message();

        return totalMessageHelper.returnTotalMessagess();
    }
}