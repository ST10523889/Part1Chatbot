import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @BeforeEach
    void setUp() {
        Message.clearMessages();
    }

    @Test
    void testMessageID() {

        Message message = new Message(
                "Hi Mike, can you join us for dinner tonight?",
                "+27718693002",
                0
        );

        assertTrue(message.checkMessageID());
    }

    @Test
    void testMessageLengthSuccess() {

        Message message = new Message(
                "Hi Mike, can you join us for dinner tonight?",
                "+27718693002",
                0
        );

        assertEquals(
                "Message ready to send.",
                message.messageLengthStatus()
        );
    }

    @Test
    void testMessageLengthFailure() {

        String longMessage = "A".repeat(251);

        Message message = new Message(
                longMessage,
                "+27718693002",
                0
        );

        assertEquals(
                "Message exceeds 250 characters by 1; please reduce the size.",
                message.messageLengthStatus()
        );
    }

    @Test
    void testValidRecipientCell() {

        Message message = new Message(
                "Hi Mike, can you join us for dinner tonight?",
                "+27718693002",
                0
        );

        assertEquals(
                "Cell phone number successfully captured.",
                message.checkRecipientCell()
        );
    }

    @Test
    void testInvalidRecipientCell() {

        Message message = new Message(
                "Hi Keegan, did you receive the payment?",
                "08575975889",
                1
        );

        assertEquals(
                "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                message.checkRecipientCell()
        );
    }

    @Test
    void testMessageHash() {

        Message message = new Message(
                "Hi Mike, can you join us for dinner tonight?",
                "+27718693002",
                0
        );

        // Give the test the ID needed for the example in the brief
        message.setMessageID("0012345678");

        assertEquals(
                "00:0:HITONIGHT",
                message.createMessageHash()
        );
    }

    @Test
    void testMessageHashesInLoop() {

        String[] messages = {
                "Hi Mike, can you join us for dinner tonight?",
                "Hello Keegan",
                "Payment received today!"
        };

        String[] messageIDs = {
                "0012345678",
                "1112345678",
                "2212345678"
        };

        String[] expectedHashes = {
                "00:0:HITONIGHT",
                "11:1:HELLOKEEGAN",
                "22:2:PAYMENTTODAY"
        };

        for (int i = 0; i < messages.length; i++) {

            Message message = new Message(
                    messages[i],
                    "+27718693002",
                    i
            );

            message.setMessageID(messageIDs[i]);

            assertEquals(
                    expectedHashes[i],
                    message.createMessageHash()
            );
        }
    }

    @Test
    void testSuccessfulSendMessage() {

        Message message = new Message(
                "Hi Mike, can you join us for dinner tonight?",
                "+27718693002",
                0
        );

        assertEquals(
                "Message successfully sent.",
                message.SentMessage(1)
        );
    }

    @Test
    void testDisregardMessage() {

        Message message = new Message(
                "Hi Keegan, did you receive the payment?",
                "+27718693002",
                1
        );

        assertEquals(
                "Press 0 to delete the message.",
                message.SentMessage(2)
        );
    }

    @Test
    void testStoreMessage() {

        Message message = new Message(
                "Hi Mike, can you join us for dinner tonight?",
                "+27718693002",
                0
        );

        assertEquals(
                "Message successfully stored.",
                message.SentMessage(3)
        );

        Path filePath = Paths.get("messages.json");

        assertTrue(Files.exists(filePath));

        try {
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            // Ignore cleanup error
        }
    }

    @Test
    void testPrintMessages() {

        Message message = new Message(
                "Hi Mike, can you join us for dinner tonight?",
                "+27718693002",
                0
        );

        message.SentMessage(1);

        String output = message.printMessages();

        assertTrue(output.contains("Message ID:"));
        assertTrue(output.contains("Message Hash:"));
        assertTrue(output.contains("Recipient:"));
        assertTrue(output.contains("Hi Mike, can you join us for dinner tonight?"));

        // Check that the information is displayed in the required order
        assertTrue(
                output.indexOf("Message ID:")
                        < output.indexOf("Message Hash:")
        );

        assertTrue(
                output.indexOf("Message Hash:")
                        < output.indexOf("Recipient:")
        );

        assertTrue(
                output.indexOf("Recipient:")
                        < output.indexOf("Message:")
        );
    }

    @Test
    void testInvalidMessageID() {

        Message message = new Message(
                "Hi Mike",
                "+27718693002",
                0
        );

        message.setMessageID("1234");

        assertFalse(message.checkMessageID());
    }

    @Test
    void testReturnTotalMessages() {

        Message message1 = new Message(
                "First message",
                "+27718693002",
                0
        );

        Message message2 = new Message(
                "Second message",
                "+27718693002",
                1
        );

        message1.SentMessage(1);
        message2.SentMessage(1);

        assertEquals(
                2,
                message1.returnTotalMessagess()
        );
    }
}