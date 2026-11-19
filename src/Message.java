
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {

    private String messageID;
    private int messageNumber;
    private String recipientCell;
    private String message;
    private String messageHash;

    private static final Path STORAGE_FILE = Paths.get("messages.json");

    private static Message[] sentMessages = new Message[0];
    private static Message[] disregardedMessages = new Message[0];
    private static Message[] storedMessages = new Message[0];

    private static String[] messageHashes = new String[0];
    private static String[] messageIDs = new String[0];

    public Message() {
    }

    public Message(String message, String recipientCell, int messageNumber) {
        this.message = message;
        this.recipientCell = recipientCell;
        this.messageNumber = messageNumber;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    private String generateMessageID() {
        long number = ThreadLocalRandom.current()
                .nextLong(0L, 10_000_000_000L);
        return String.format("%010d", number);
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.matches("\\d{10}");
    }

    public String checkRecipientCell() {
        if (recipientCell != null
                && recipientCell.matches("^\\+27[0-9]{9}$")) {
            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or does not "
                + "contain an international code. Please correct the "
                + "number and try again.";
    }

    public boolean checkMessageLength() {
        return message != null && message.length() <= 250;
    }

    public String messageLengthStatus() {
        if (checkMessageLength()) {
            return "Message ready to send.";
        }

        int over = message == null ? 250 : message.length() - 250;

        return "Message exceeds 250 characters by " + over
                + "; please reduce the size.";
    }

    public String createMessageHash() {
        if (!checkMessageID() || message == null
                || message.trim().isEmpty()) {
            messageHash = "";
            return messageHash;
        }

        String[] words = message.trim().split("\\s+");
        String first = cleanWord(words[0]);
        String last = cleanWord(words[words.length - 1]);

        messageHash = messageID.substring(0, 2)
                + ":" + messageNumber + ":"
                + (first + last).toUpperCase();

        return messageHash;
    }

    public String SentMessage() {
        return "1) Send Message\n"
                + "2) Disregard Message\n"
                + "3) Store Message to send later";
    }

    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages = appendUnique(sentMessages, this);
                addHashAndID(this);
                return "Message successfully sent.";

            case 2:
                disregardedMessages =
                        appendUnique(disregardedMessages, this);
                addHashAndID(this);
                return "Message disregarded.";

            case 3:
                storedMessages = appendUnique(storedMessages, this);
                addHashAndID(this);
                return storeMessagesToJson();

            default:
                return "Invalid option. Please choose 1, 2, or 3.";
        }
    }

    private static void addHashAndID(Message item) {
        if (item.messageHash != null && !item.messageHash.isEmpty()) {
            messageHashes = appendUnique(messageHashes, item.messageHash);
        }

        if (item.messageID != null) {
            messageIDs = appendUnique(messageIDs, item.messageID);
        }
    }

    private static Message[] appendUnique(
            Message[] original, Message item) {
        for (Message existing : original) {
            if (existing == item) {
                return original;
            }
        }

        Message[] result = Arrays.copyOf(original, original.length + 1);
        result[result.length - 1] = item;
        return result;
    }

    private static String[] appendUnique(
            String[] original, String item) {
        for (String existing : original) {
            if (existing.equals(item)) {
                return original;
            }
        }

        String[] result = Arrays.copyOf(original, original.length + 1);
        result[result.length - 1] = item;
        return result;
    }

    public String printMessages() {
        return formatMessages(sentMessages, false);
    }

    public String printDisregardedMessages() {
        return formatMessages(disregardedMessages, false);
    }

    public String printStoredMessages() {
        return formatMessages(storedMessages, true);
    }

    private String formatMessages(
            Message[] messages, boolean includeSender) {
        if (messages.length == 0) {
            return "No messages found.";
        }

        StringBuilder output = new StringBuilder();

        for (int i = 0; i < messages.length; i++) {
            Message current = messages[i];

            if (i > 0) {
                output.append("\n\n");
            }

            if (includeSender) {
                output.append("Sender: QuickChat user\n");
            }

            output.append("Message ID: ")
                    .append(current.messageID)
                    .append("\nMessage Hash: ")
                    .append(current.messageHash)
                    .append("\nRecipient: ")
                    .append(current.recipientCell)
                    .append("\nMessage: ")
                    .append(current.message);
        }

        return output.toString();
    }

    public int returnTotalMessagess() {
        return sentMessages.length;
    }

    public String getLongestStoredMessage() {
        if (storedMessages.length == 0) {
            return "No stored messages found.";
        }

        Message longest = storedMessages[0];

        for (Message current : storedMessages) {
            if (current.message != null
                    && (longest.message == null
                    || current.message.length() > longest.message.length())) {
                longest = current;
            }
        }

        return longest.message == null
                ? "No stored messages found."
                : longest.message;
    }

    // Search stored, sent, and disregarded messages by their ID.
    // Keeps the exact recipient-and-message output expected by MessageTest.
    public String searchMessageByID(String id) {
        if (id == null || id.trim().isEmpty()) {
            return "Please enter a valid message ID.";
        }

        String searchID = id.trim();

        for (Message current : storedMessages) {
            if (current.messageID != null
                    && current.messageID.equals(searchID)) {
                return formatSearchResult(current);
            }
        }

        for (Message current : sentMessages) {
            if (current.messageID != null
                    && current.messageID.equals(searchID)) {
                return formatSearchResult(current);
            }
        }

        for (Message current : disregardedMessages) {
            if (current.messageID != null
                    && current.messageID.equals(searchID)) {
                return formatSearchResult(current);
            }
        }

        return "No message found for message ID: " + searchID;
    }

    private String formatSearchResult(Message current) {
        return "Recipient: " + current.recipientCell
                + "\nMessage: " + current.message;
    }

    public String searchMessagesByRecipient(String recipient) {
        if (recipient == null) {
            return "No stored messages found for that recipient.";
        }

        String target = recipient.trim();
        StringBuilder output = new StringBuilder();

        for (Message current : storedMessages) {
            if (current.recipientCell != null
                    && current.recipientCell.equals(target)) {
                if (output.length() > 0) {
                    output.append("\n\n");
                }

                output.append("Message ID: ")
                        .append(current.messageID)
                        .append("\nMessage Hash: ")
                        .append(current.messageHash)
                        .append("\nRecipient: ")
                        .append(current.recipientCell)
                        .append("\nMessage: ")
                        .append(current.message);
            }
        }

        return output.length() == 0
                ? "No stored messages found for that recipient."
                : output.toString();
    }

    public static boolean deleteDisregardedMessageByID(String id) {
        for (int i = 0; i < disregardedMessages.length; i++) {
            Message current = disregardedMessages[i];

            if (current.messageID != null
                    && current.messageID.equals(id)) {
                disregardedMessages =
                        removeAt(disregardedMessages, i);
                rebuildHashAndIdArrays();
                return true;
            }
        }

        return false;
    }

    public String deleteMessageByHash(String hash) {
        if (hash == null) {
            return "No stored message found with that message hash.";
        }

        String target = hash.trim();

        for (int i = 0; i < storedMessages.length; i++) {
            Message current = storedMessages[i];

            if (current.messageHash != null
                    && current.messageHash.equals(target)) {
                String deletedText = current.message;

                storedMessages = removeAt(storedMessages, i);
                rebuildHashAndIdArrays();

                String saveResult = storeMessagesToJson();

                if (!"Stored messages saved to messages.json."
                        .equals(saveResult)) {
                    return "The message was removed from this session, "
                            + "but the file could not be updated. "
                            + saveResult;
                }

                return "Message: \"" + deletedText
                        + "\" successfully deleted.";
            }
        }

        return "No stored message found with that message hash.";
    }

    private static Message[] removeAt(
            Message[] original, int index) {
        Message[] result = new Message[original.length - 1];

        System.arraycopy(original, 0, result, 0, index);
        System.arraycopy(original, index + 1, result, index,
                original.length - index - 1);

        return result;
    }

    private static String[] removeValue(
            String[] original, String value) {
        List<String> values = new ArrayList<>(Arrays.asList(original));
        values.remove(value);
        return values.toArray(new String[0]);
    }

    private static String storeMessagesToJson() {
        StringBuilder json = new StringBuilder("[\n");

        for (int i = 0; i < storedMessages.length; i++) {
            Message current = storedMessages[i];

            if (i > 0) {
                json.append(",\n");
            }

            json.append("  {\n")
                    .append("    \"messageID\": ")
                    .append(jsonString(current.messageID)).append(",\n")
                    .append("    \"messageNumber\": ")
                    .append(current.messageNumber).append(",\n")
                    .append("    \"recipientCell\": ")
                    .append(jsonString(current.recipientCell)).append(",\n")
                    .append("    \"message\": ")
                    .append(jsonString(current.message)).append(",\n")
                    .append("    \"messageHash\": ")
                    .append(jsonString(current.messageHash)).append("\n")
                    .append("  }");
        }

        json.append("\n]\n");

        try {
            Files.writeString(
                    STORAGE_FILE, json.toString(), StandardCharsets.UTF_8);
            return "Stored messages saved to messages.json.";
        } catch (IOException e) {
            return "Message could not be stored: " + e.getMessage();
        }
    }

    private static String jsonString(String value) {
        if (value == null) {
            return "null";
        }

        StringBuilder result = new StringBuilder("\"");

        for (char c : value.toCharArray()) {
            switch (c) {
                case '"': result.append("\\\""); break;
                case '\\': result.append("\\\\"); break;
                case '\n': result.append("\\n"); break;
                case '\r': result.append("\\r"); break;
                case '\t': result.append("\\t"); break;
                case '\b': result.append("\\b"); break;
                case '\f': result.append("\\f"); break;
                default:
                    if (c < 32) {
                        result.append(String.format("\\u%04x", (int) c));
                    } else {
                        result.append(c);
                    }
            }
        }

        return result.append('"').toString();
    }

    public static String loadStoredMessagesFromJson() {
        if (!Files.exists(STORAGE_FILE)) {
            storedMessages = new Message[0];
            rebuildHashAndIdArrays();
            return "No messages.json file found yet.";
        }

        try {
            String json = Files.readString(
                    STORAGE_FILE, StandardCharsets.UTF_8);

            List<Message> loaded = new ArrayList<>();

            int objectStart = -1;
            int depth = 0;
            boolean inString = false;
            boolean escaped = false;

            for (int i = 0; i < json.length(); i++) {
                char c = json.charAt(i);

                if (inString) {
                    if (escaped) {
                        escaped = false;
                    } else if (c == '\\') {
                        escaped = true;
                    } else if (c == '"') {
                        inString = false;
                    }
                    continue;
                }

                if (c == '"') {
                    inString = true;
                } else if (c == '{') {
                    if (depth == 0) {
                        objectStart = i;
                    }
                    depth++;
                } else if (c == '}') {
                    depth--;

                    if (depth == 0 && objectStart >= 0) {
                        String object = json.substring(objectStart, i + 1);
                        Message parsed = parseMessage(object);

                        if (parsed != null) {
                            loaded.add(parsed);
                        }

                        objectStart = -1;
                    }
                }
            }

            storedMessages = loaded.toArray(new Message[0]);
            rebuildHashAndIdArrays();

            return "Stored messages loaded: " + storedMessages.length;
        } catch (IOException | RuntimeException e) {
            return "Could not read messages.json: " + e.getMessage();
        }
    }

    private static Message parseMessage(String object) {
        Message result = new Message();

        result.messageID = readJsonString(object, "messageID");
        result.messageNumber = Integer.parseInt(
                readJsonNumber(object, "messageNumber"));
        result.recipientCell = readJsonString(object, "recipientCell");
        result.message = readJsonString(object, "message");
        result.messageHash = readJsonString(object, "messageHash");

        if (result.messageHash == null
                && result.messageID != null
                && result.message != null) {
            result.createMessageHash();
        }

        return result;
    }

    private static String readJsonNumber(String object, String key) {
        Pattern pattern = Pattern.compile(
                "\"" + Pattern.quote(key) + "\"\\s*:\\s*(-?\\d+)");
        Matcher matcher = pattern.matcher(object);

        return matcher.find() ? matcher.group(1) : "0";
    }

    private static String readJsonString(String object, String key) {
        Pattern pattern = Pattern.compile(
                "\"" + Pattern.quote(key)
                        + "\"\\s*:\\s*(null|\"(?:\\\\.|[^\"\\\\])*\")");
        Matcher matcher = pattern.matcher(object);

        if (!matcher.find() || "null".equals(matcher.group(1))) {
            return null;
        }

        String raw = matcher.group(1);
        StringBuilder result = new StringBuilder();

        for (int i = 1; i < raw.length() - 1; i++) {
            char c = raw.charAt(i);

            if (c != '\\') {
                result.append(c);
                continue;
            }

            i++;
            if (i >= raw.length() - 1) {
                break;
            }

            char escaped = raw.charAt(i);

            switch (escaped) {
                case '"': result.append('"'); break;
                case '\\': result.append('\\'); break;
                case '/': result.append('/'); break;
                case 'n': result.append('\n'); break;
                case 'r': result.append('\r'); break;
                case 't': result.append('\t'); break;
                case 'b': result.append('\b'); break;
                case 'f': result.append('\f'); break;
                case 'u':
                    if (i + 4 < raw.length()) {
                        String hex = raw.substring(i + 1, i + 5);
                        result.append((char) Integer.parseInt(hex, 16));
                        i += 4;
                    }
                    break;
                default: result.append(escaped);
            }
        }

        return result.toString();
    }

    private static void rebuildHashAndIdArrays() {
        messageHashes = new String[0];
        messageIDs = new String[0];

        for (Message current : sentMessages) {
            addHashAndID(current);
        }

        for (Message current : disregardedMessages) {
            addHashAndID(current);
        }

        for (Message current : storedMessages) {
            addHashAndID(current);
        }
    }

    public static Message[] getSentMessagesArray() {
        return Arrays.copyOf(sentMessages, sentMessages.length);
    }

    public static Message[] getDisregardedMessagesArray() {
        return Arrays.copyOf(disregardedMessages,
                disregardedMessages.length);
    }

    public static Message[] getStoredMessagesArray() {
        return Arrays.copyOf(storedMessages, storedMessages.length);
    }

    public static String[] getMessageHashesArray() {
        return Arrays.copyOf(messageHashes, messageHashes.length);
    }

    public static String[] getMessageIDsArray() {
        return Arrays.copyOf(messageIDs, messageIDs.length);
    }

    private String cleanWord(String word) {
        return word.replaceAll("^[^A-Za-z0-9]+|[^A-Za-z0-9]+$", "");
    }

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

    public void setMessageID(String messageID) {
        this.messageID = messageID;
        this.messageHash = createMessageHash();
    }

    public static void clearMessages() {
        sentMessages = new Message[0];
        disregardedMessages = new Message[0];
        storedMessages = new Message[0];
        messageHashes = new String[0];
        messageIDs = new String[0];
    }

    public static void startNewSession() {
        clearMessages();

        try {
            Files.deleteIfExists(STORAGE_FILE);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not clear old messages: " + e.getMessage(), e);
        }
    }

    public static void setStoredMessagesForTesting(Message... messages) {
        storedMessages = Arrays.copyOf(messages, messages.length);
        rebuildHashAndIdArrays();
    }
}
