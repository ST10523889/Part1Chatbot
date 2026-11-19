
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @BeforeEach
    void setUp() {
        // Clear messages before each test.
        Message.clearMessages();

        try {
            Files.deleteIfExists(Paths.get("messages.json"));
        } catch (Exception ignored) {
            // The file might not exist yet.
        }
    }

    @Test
    void testMessageID() {
        assertTrue(
                new Message("Hi, whos this again?", "+27834557896", 0)
                        .checkMessageID());
    }

    @Test
    void testMessageLengthSuccess() {
        assertEquals(
                "Message ready to send.",
                new Message(
                        "Did you leave your charger at my place?",
                        "+27786082033", 0).messageLengthStatus());
    }

    @Test
    void testMessageLengthFailure() {
        assertEquals(
                "Message exceeds 250 characters by 1; please reduce the size.",
                new Message(
                        "A".repeat(251), "+27786082033", 0)
                        .messageLengthStatus());
    }

    @Test
    void testValidRecipientCell() {
        assertEquals(
                "Cell phone number successfully captured.",
                new Message(
                        "hows the kids bro.. they good?",
                        "+27823568788", 0).checkRecipientCell());
    }

    @Test
    void testInvalidRecipientCell() {
        // This number does not have the correct format.
        assertFalse(
                new Message(
                        "who u with rn?", "0636762333", 0)
                        .checkRecipientCell()
                        .equals("Cell phone number successfully captured."));
    }

    @Test
    void testMessageHash() {
        Message message = new Message(
                "what time should i get there?",
                "+27823568788", 0);

        message.setMessageID("0012345678");

        // The hash uses the first and last words of the message.
        assertEquals("00:0:WHATTHERE", message.createMessageHash());
    }

    @Test
    void testSentArrayContainsExpectedData() {
        Message first = new Message(
                "Hi, whos this again?",
                "+27786082033", 0);

        Message second = new Message(
                "what time should i get there?",
                "+27823568788", 3);

        first.SentMessage(1);
        second.SentMessage(1);

        assertEquals(2, Message.getSentMessagesArray().length);

        assertEquals(
                "Hi, whos this again?",
                Message.getSentMessagesArray()[0].getMessage());

        assertEquals(
                "what time should i get there?",
                Message.getSentMessagesArray()[1].getMessage());
    }

    @Test
    void testLongestStoredMessage() {
        Message shortMessage = new Message(
                "who u with rn?",
                "+27636762333", 4);

        Message longMessage = new Message(
                "Coach said training tmr is cancelled, i dont think im coming bro, you still going?",
                "+27823568788", 1);

        Message.setStoredMessagesForTesting(shortMessage, longMessage);

        assertEquals(
                "Coach said training tmr is cancelled, i dont think im coming bro, you still going?",
                shortMessage.getLongestStoredMessage());
    }

    @Test
    void testSearchByMessageID() {
        Message message = new Message(
                "Did you leave your charger at my place?",
                "+27786082033", 1);

        message.setMessageID("1112345678");
        Message.setStoredMessagesForTesting(message);

        assertEquals(
                "Recipient: +27786082033\n"
                        + "Message: Did you leave your charger at my place?",
                message.searchMessageByID("1112345678"));
    }

    @Test
    void testSearchByMessageIDWithSpaces() {
        Message message = new Message(
                "How many ppl are gonna be there? wanna know how many drinks to bring",
                "+27823568788", 1);

        message.setMessageID("2222345678");
        Message.setStoredMessagesForTesting(message);

        assertEquals(
                "Recipient: +27823568788\n"
                        + "Message: How many ppl are gonna be there? wanna know how many drinks to bring",
                message.searchMessageByID(" 2222345678 "));
    }

    @Test
    void testSearchByInvalidMessageID() {
        Message message = new Message(
                "ur testing me bruv",
                "+27636762333", 1);

        Message.setStoredMessagesForTesting(message);

        assertTrue(
                message.searchMessageByID("0000000000")
                        .startsWith("No message found"));
    }

    @Test
    void testSearchAllStoredMessagesForRecipient() {
        Message one = new Message(
                "Did you leave your charger at my place?",
                "+27786082033", 1);

        Message two = new Message(
                "who u with rn?",
                "+27786082033", 4);

        Message.setStoredMessagesForTesting(one, two);

        String result = one.searchMessagesByRecipient("+27786082033");

        assertTrue(result.contains(
                "Did you leave your charger at my place?"));

        assertTrue(result.contains("who u with rn?"));
    }

    @Test
    void testDeleteStoredMessageByHash() {
        Message message = new Message(
                "ur testing me bruv",
                "+27823568788", 1);

        message.setMessageID("1112345678");
        Message.setStoredMessagesForTesting(message);

        String result = message.deleteMessageByHash(
                message.getMessageHash());

        assertTrue(result.contains("successfully deleted"));
        assertEquals(0, Message.getStoredMessagesArray().length);
    }

    @Test
    void testStoreAndReadJson() {
        Message message = new Message(
                "Hi, whos this again? He said \"hello\".",
                "+27786082033", 4);

        assertEquals(
                "Stored messages saved to messages.json.",
                message.SentMessage(3));

        Message.clearMessages();

        assertTrue(
                Message.loadStoredMessagesFromJson()
                        .startsWith("Stored messages loaded: 1"));

        assertEquals(
                "Hi, whos this again? He said \"hello\".",
                Message.getStoredMessagesArray()[0].getMessage());

        assertTrue(Files.exists(Path.of("messages.json")));
    }

    @Test
    void testPrintMessagesReport() {
        Message message = new Message(
                "hows the kids bro.. they good?",
                "+27636762333", 0);

        message.SentMessage(1);

        String report = message.printMessages();

        assertTrue(report.contains("Message Hash:"));
        assertTrue(report.contains("Recipient:"));
        assertTrue(report.contains("Message:"));
    }

    @Test
    void testDisregardedArrayPopulated() {
        Message message = new Message(
                "Coach said training tmr is cancelled, i dont think im coming bro, you still going?",
                "+27823568788", 2);

        message.SentMessage(2);

        assertEquals(1, Message.getDisregardedMessagesArray().length);
    }

    @Test
    void testDeleteDisregardedMessageByID() {
        Message message = new Message(
                "what time should i get there?",
                "+27786082033", 2);

        message.SentMessage(2);

        assertTrue(
                Message.deleteDisregardedMessageByID(
                        message.getMessageID()));

        assertEquals(0, Message.getDisregardedMessagesArray().length);
    }
}
