import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testValidUsernameFormat() {
        // test valid username: has an underscore and is 5 chars
        Login userLogin = new Login("amg_5", "M3rc3d3s$$!", "+27786082033", "Sipho", "Ndlovu");
        assertTrue(userLogin.checkUserName());
    }

    @Test
    void testInvalidUsernameFormat() {
        // test invalid: no underscore and way over the 5 char limit
        Login userLogin = new Login("mercedesbenz", "M3rc3d3s$$!", "+27786082033", "Sipho", "Ndlovu");
        assertFalse(userLogin.checkUserName());
    }

    @Test
    void testStrongPassword() {
        // test password that passes all rules (caps, numbers, special chars)
        Login userLogin = new Login("amg_5", "M3rc3d3s$$!", "+27786082033", "Sipho", "Ndlovu");
        assertTrue(userLogin.checkPasswordComplexity());
    }

    @Test
    void testWeakPassword() {
        // test weak password missing caps and special chars
        Login userLogin = new Login("amg_5", "weakpass", "+27786082033", "Sipho", "Ndlovu");
        assertFalse(userLogin.checkPasswordComplexity());
    }

    @Test
    void testSuccessfulLogin() {
        // test correct login details should return true
        Login userLogin = new Login("amg_5", "M3rc3d3s$$!", "+27786082033", "Sipho", "Ndlovu");
        assertTrue(userLogin.loginUser("amg_5", "M3rc3d3s$$!"));
    }

    @Test
    void testFailedLogin() {
        // test wrong password should fail login
        Login userLogin = new Login("amg_5", "M3rc3d3s$$!", "+27786082033", "Sipho", "Ndlovu");
        assertFalse(userLogin.loginUser("amg_5", "wrongpass123"));
    }
}