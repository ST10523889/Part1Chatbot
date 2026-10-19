import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class Message {

    private String messageID;
    private int messageNumber;
    private String recipientCell;
    private String message;
    private String messageHash;

    // Stores the messages that were successfully sent while the program is running
    private static final List<Message> sentMessages = new ArrayList<>();

    // Stores the messages that were selected to be sent later
    private static final List<Message> storedMessages = new ArrayList<>();

    // Constructor
    public Message(String message, String recipientCell, int messageNumber) {
        this.message = message;
        this.recipientCell = recipientCell;
        this.messageNumber = messageNumber;

        // Automatically generates the 10 digit Message ID
        this.messageID = generateMessageID();

        // Automatically creates the Message Hash
        this.messageHash = createMessageHash();
    }

    // Empty constructor
    public Message() {
    }

    // Generates a random 10 digit Message ID
    private String generateMessageID() {
        long randomNumber = ThreadLocalRandom.current()
                .nextLong(0L, 10_000_000_000L);

        return String.format("%010d", randomNumber);
    }

    // Checks that the Message ID contains exactly 10 digits
    public boolean checkMessageID() {
        return messageID != null
                && Pattern.matches("\\d{10}", messageID);
    }

    // Checks the recipient's cell number
    // The assignment's sample uses a South African international phone number.
    public String checkRecipientCell() {

        String cellPhoneRegex = "^\\+27[0-9]{9}$";

        if (recipientCell != null
                && Pattern.matches(cellPhoneRegex, recipientCell)) {

            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    // Checks that the message is 250 characters or less
    public boolean checkMessageLength() {

        return message != null && message.length() <= 250;
    }

    // Returns the message length result required by the task
    public String messageLengthStatus() {

        if (checkMessageLength()) {
            return "Message ready to send.";
        }

        int charactersOver;

        if (message == null) {
            charactersOver = 250;
        } else {
            charactersOver = message.length() - 250;
        }

        return "Message exceeds 250 characters by "
                + charactersOver
                + "; please reduce the size.";
    }

    // Creates the Message Hash
    public String createMessageHash() {

        if (!checkMessageID()
                || message == null
                || message.trim().isEmpty()) {

            return "";
        }

        // Splits the message into words
        String[] words = message.trim().split("\\s+");

        // Gets first and last words
        String firstWord = cleanWord(words[0]);
        String lastWord = cleanWord(words[words.length - 1]);

        // First two characters of Message ID
        String firstTwoIDNumbers = messageID.substring(0, 2);

        // Creates hash
        messageHash = firstTwoIDNumbers
                + ":"
                + messageNumber
                + ":"
                + (firstWord + lastWord).toUpperCase();

        return messageHash;
    }

    // No argument version
    public String SentMessage() {
        return "1) Send Message\n"
                + "2) Disregard Message\n"
                + "3) Store Message to send later";
    }

    // Handles Send, Disregard and Store
    public String SentMessage(int choice) {

        switch (choice) {

            case 1:
                // Send Message
                if (!sentMessages.contains(this)) {
                    sentMessages.add(this);
                }

                return "Message successfully sent.";

            case 2:
                // Disregard Message
                return "Press 0 to delete the message.";

            case 3:
                // Store Message
                return storeMessage();

            default:
                return "Invalid option. Please choose 1, 2, or 3.";
        }
    }

    // Returns all successfully sent messages
    public String printMessages() {

        if (sentMessages.isEmpty()) {
            return "No messages have been sent.";
        }

        StringBuilder output = new StringBuilder();

        for (Message currentMessage : sentMessages) {

            output.append("Message ID: ")
                    .append(currentMessage.messageID)
                    .append("\n");

            output.append("Message Hash: ")
                    .append(currentMessage.messageHash)
                    .append("\n");

            output.append("Recipient: ")
                    .append(currentMessage.recipientCell)
                    .append("\n");

            output.append("Message: ")
                    .append(currentMessage.message)
                    .append("\n\n");
        }

        return output.toString().trim();
    }

    // Returns the total number of messages successfully sent
    public int returnTotalMessagess() {

        return sentMessages.size();
    }

    // Stores messages in a JSON file
    public String storeMessage() {

        try {

            if (!storedMessages.contains(this)) {
                storedMessages.add(this);
            }

            Path filePath = Paths.get("messages.json");

            /*
             * JSON storage is implemented using standard Java file I/O.
             * Reference: Java Files.newBufferedWriter documentation.
             * https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/nio/file/Files.html
             */

            try (BufferedWriter writer =
                         Files.newBufferedWriter(filePath)) {

                writer.write("[\n");

                for (int i = 0; i < storedMessages.size(); i++) {

                    writer.write("  ");

                    writer.write(
                            storedMessages.get(i).toJson()
                    );

                    if (i < storedMessages.size() - 1) {
                        writer.write(",");
                    }

                    writer.write("\n");
                }

                writer.write("]");
            }

            return "Message successfully stored.";

        } catch (IOException e) {

            return "Message could not be stored.";
        }
    }

    // Converts a message into JSON format
    private String toJson() {

        return "{"
                + "\"messageID\":\""
                + escapeJson(messageID)
                + "\","

                + "\"messageNumber\":"
                + messageNumber
                + ","

                + "\"recipient\":\""
                + escapeJson(recipientCell)
                + "\","

                + "\"message\":\""
                + escapeJson(message)
                + "\","

                + "\"messageHash\":\""
                + escapeJson(messageHash)
                + "\""
                + "}";
    }

    // Prevents JSON problems with special characters
    private String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\t", "\\t");
    }

    // Removes punctuation from the beginning and end of a word
    private String cleanWord(String word) {

        return word.replaceAll(
                "^[^A-Za-z0-9]+|[^A-Za-z0-9]+$",
                ""
        );
    }

    // Getters
    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getRecipientCell() {
        return recipientCell;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageHash() {
        return messageHash;
    }

    // Used for the unit-test hash example
    public void setMessageID(String messageID) {

        this.messageID = messageID;
        this.messageHash = createMessageHash();
    }

    // Clears lists before the unit tests
    public static void clearMessages() {

        sentMessages.clear();
        storedMessages.clear();
    }
}