
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testValidUsernameFormat() {
        Login user = new Login(
                "azu_k", "Rosebank1!", "+27786082033", "Azu", "Keli");

        assertTrue(user.checkUserName());
    }

    @Test
    void testInvalidUsernameFormat() {
        // This username is too long.
        Login user = new Login(
                "maguir_3", "manUTD1st?", "+27823568788",
                "Harry", "Maguire");

        assertFalse(user.checkUserName());
    }

    @Test
    void testAnotherValidUsername() {
        Login user = new Login(
                "p0_gz", "B3stmidf!elder", "+27636762333",
                "Paul", "Pogba");

        assertTrue(user.checkUserName());
    }

    @Test
    void testStrongPassword() {
        Login user = new Login(
                "azu_k", "Rosebank1!", "+27786082033", "Azu", "Keli");

        assertTrue(user.checkPasswordComplexity());
    }

    @Test
    void testWeakPassword() {
        // This password has no capital letter or special character.
        Login user = new Login(
                "p0_gz", "b3stmidfelder", "+27636762333",
                "Paul", "Pogba");

        assertFalse(user.checkPasswordComplexity());
    }

    @Test
    void testHarryPasswordIsStrong() {
        // This password includes uppercase and lowercase letters,
        // numbers, a special character, and enough characters.
        Login user = new Login(
                "maguir_3", "Harry2026!", "+27823568788",
                "Harry", "Maguire");

        assertTrue(user.checkPasswordComplexity());
    }

    @Test
    void testValidPhoneNumber() {
        Login user = new Login(
                "azu_k", "Rosebank1!", "+27786082033", "Azu", "Keli");

        assertTrue(user.checkCellPhoneNumber());
    }

    @Test
    void testInvalidPhoneNumber() {
        // This number does not use the correct format.
        Login user = new Login(
                "azu_k", "Rosebank1!", "0786082033", "Azu", "Keli");

        assertFalse(user.checkCellPhoneNumber());
    }

    @Test
    void testSuccessfulLogin() {
        Login user = new Login(
                "azu_k", "Rosebank1!", "+27786082033", "Azu", "Keli");

        assertTrue(user.loginUser("azu_k", "Rosebank1!"));
    }

    @Test
    void testFailedLogin() {
        Login user = new Login(
                "azu_k", "Rosebank1!", "+27786082033", "Azu", "Keli");

        assertFalse(user.loginUser("azu_k", "WrongPass1!"));
    }

    @Test
    void testRegistrationSuccess() {
        Login user = new Login(
                "azu_k", "Rosebank1!", "+27786082033", "Azu", "Keli");

        assertEquals(
                "Username successfully captured.\n"
                        + "Password successfully captured.\n"
                        + "Cell phone number successfully added.",
                user.registerUser());
    }
}
