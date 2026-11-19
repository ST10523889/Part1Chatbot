
import java.util.Scanner;

public class Main {

    // This reads what the user types.
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        // Start with no old messages.
        Message.startNewSession();

        System.out.println("=== Registration ===");

        String firstName = askText("Enter your first name:");
        String lastName = askText("Enter your last name:");
        String username = askText("Enter your username:");
        String password = askText("Enter your password:");
        String phone = askText(
                "Enter your phone number (+27 followed by 9 digits):");

        Login user = new Login(
                username, password, phone, firstName, lastName);

        System.out.println("\n" + user.registerUser());

        if (!(user.checkUserName()
                && user.checkPasswordComplexity()
                && user.checkCellPhoneNumber())) {

            System.out.println(
                    "Registration failed. Please restart and correct your details.");
            return;
        }

        System.out.println("\n=== Login ===");

        String enteredUsername = askText("Enter username for login:");
        String enteredPassword = askText("Enter password for login:");

        boolean loggedIn = user.loginUser(
                enteredUsername, enteredPassword);

        System.out.println(user.returnLoginStatus(loggedIn));

        if (!loggedIn) {
            return;
        }

        runQuickChat();
    }

    // This asks the user to type something.
    private static String askText(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }

    // This shows the main menu.
    private static void runQuickChat() {

        System.out.println("\nWelcome to QuickChat.");

        Integer maxMessages = askPositiveInteger(
                "How many messages would you like to enter?");

        if (maxMessages == null) {
            return;
        }

        int processed = 0;
        Message helper = new Message();

        while (true) {

            System.out.println(
                    "\nChoose an option:\n"
                            + "1) Send Messages\n"
                            + "2) Show recently sent messages\n"
                            + "3) Quit\n"
                            + "4) Stored Messages");

            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1": {

                    if (processed >= maxMessages) {
                        System.out.println(
                                "You have reached your message limit.\n"
                                        + "Messages sent: "
                                        + helper.returnTotalMessagess());
                        break;
                    }

                    boolean keepSending = true;

                    while (processed < maxMessages && keepSending) {

                        Message message = createMessage(processed);

                        if (message == null) {
                            System.out.println("Message entry cancelled.");
                            break;
                        }

                        System.out.println(message.messageLengthStatus());

                        System.out.println(
                                "Message ID: " + message.getMessageID()
                                        + "\nMessage Hash: "
                                        + message.createMessageHash());

                        Integer action = askMessageAction();

                        if (action == null) {
                            System.out.println("Message action cancelled.");
                            break;
                        }

                        String result = message.SentMessage(action);
                        System.out.println(result);

                        if (action == 1) {
                            System.out.println(
                                    "Message ID: " + message.getMessageID()
                                            + "\nMessage Hash: "
                                            + message.getMessageHash()
                                            + "\nRecipient: "
                                            + message.getRecipientCell()
                                            + "\nMessage: "
                                            + message.getMessage());

                        } else if (action == 2) {
                            System.out.println("Message disregarded.");

                        } else if (action == 3) {
                            System.out.println("Message stored successfully.");
                        }

                        processed++;

                        if (processed < maxMessages) {
                            String answer = askText(
                                    "Would you like to enter another message? (yes/no):");

                            keepSending = answer.trim().equalsIgnoreCase("yes");
                        }
                    }

                    System.out.println(
                            "Total messages sent: "
                                    + helper.returnTotalMessagess());
                    break;
                }

                case "2":
                    System.out.println(helper.printMessages());
                    break;

                case "3":
                    System.out.println("Thank you for using QuickChat.");
                    return;

                case "4":
                    storedMessagesMenu(helper);
                    break;

                default:
                    System.out.println("Please select 1, 2, 3, or 4.");
            }
        }
    }

    // This lets the user create a message.
    private static Message createMessage(int messageNumber) {

        String recipient;

        while (true) {
            recipient = askText("Enter recipient (+27 followed by 9 digits):");
            recipient = recipient.trim();

            Message check = new Message(
                    "Temporary message", recipient, messageNumber);

            if ("Cell phone number successfully captured."
                    .equals(check.checkRecipientCell())) {
                break;
            }

            System.out.println(check.checkRecipientCell());
        }

        while (true) {
            String text = askText(
                    "Enter your message (maximum 250 characters):");

            Message check = new Message(text, recipient, messageNumber);

            if (!text.trim().isEmpty() && check.checkMessageLength()) {
                return new Message(text, recipient, messageNumber);
            }

            System.out.println(
                    "Please enter a non-empty message of no more than 250 characters.\n"
                            + check.messageLengthStatus());
        }
    }

    // This asks what should happen to the message.
    private static Integer askMessageAction() {

        while (true) {
            System.out.println(
                    "\nChoose what to do with this message:\n"
                            + "1) Send Message\n"
                            + "2) Disregard Message\n"
                            + "3) Store Message to send later");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    return 1;
                case "2":
                    return 2;
                case "3":
                    return 3;
                default:
                    System.out.println("Please select 1, 2, or 3.");
            }
        }
    }

    // This makes sure the entered number is bigger than zero.
    private static Integer askPositiveInteger(String prompt) {

        while (true) {
            String input = askText(prompt);

            try {
                int number = Integer.parseInt(input.trim());

                if (number > 0) {
                    return number;
                }

            } catch (NumberFormatException ignored) {
                // The number entered was not typed correctly.
            }

            System.out.println("Please enter a whole number greater than zero.");
        }
    }

    // This shows the stored message options.
    private static void storedMessagesMenu(Message helper) {

        while (true) {

            System.out.println(
                    "\nStored Messages - Choose an option:\n"
                            + "1) Display sender and recipient\n"
                            + "2) Display the longest stored message\n"
                            + "3) Search by message ID\n"
                            + "4) Search messages for a recipient\n"
                            + "5) Delete a stored message using its hash\n"
                            + "6) Display full stored-message report\n"
                            + "7) Return to main menu");

            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1": {
                    Message[] messages = Message.getStoredMessagesArray();

                    if (messages.length == 0) {
                        System.out.println("No stored messages found.");
                        break;
                    }

                    for (int i = 0; i < messages.length; i++) {
                        System.out.println(
                                "\nStored Message " + (i + 1)
                                        + "\nSender: QuickChat user"
                                        + "\nRecipient: "
                                        + messages[i].getRecipientCell());
                    }
                    break;
                }

                case "2":
                    System.out.println(helper.getLongestStoredMessage());
                    break;

                case "3": {
                    String id = askText(
                            "Enter the message ID to search for:");

                    System.out.println(helper.searchMessageByID(id.trim()));
                    break;
                }

                case "4": {
                    String recipient = askText(
                            "Enter recipient number (+27 followed by 9 digits):");

                    System.out.println(
                            helper.searchMessagesByRecipient(recipient.trim()));
                    break;
                }

                case "5": {
                    String hash = askText("Enter the message hash to delete:");
                    String confirm = askText(
                            "Delete the stored message with hash "
                                    + hash.trim() + "? (yes/no):");

                    if (confirm.trim().equalsIgnoreCase("yes")) {
                        System.out.println(
                                helper.deleteMessageByHash(hash.trim()));
                    } else {
                        System.out.println("Deletion cancelled.");
                    }
                    break;
                }

                case "6":
                    System.out.println(helper.printStoredMessages());
                    break;

                case "7":
                    return;

                default:
                    System.out.println("Please select an option from 1 to 7.");
            }
        }
    }
}
