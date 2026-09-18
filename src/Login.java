import java.util.regex.Pattern;

public class Login {
    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;

    // Setting up the user details
    public Login(String username, String password, String phoneNumber, String firstName, String lastName) {
        // Using 'this' so the variables don't get mixed up
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Empty constructor just in case
    public Login() {
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }
    public void setCellPhoneNumber(String cellPhoneNumber) {
        this.cellPhoneNumber = cellPhoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Making sure the username has an _ and is max 5 chars
    public boolean checkUserName() {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity() {
        // Checking password rules (8 chars, capital, number, special char)
        String passwordRegex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=!._-]).{8,}$"; // Used ai to help figure out this regex pattern
        return password != null && Pattern.matches(passwordRegex, password);
    }

    // Checking if it's a valid SA phone number
    public boolean checkCellPhoneNumber() {
        // Needs to start with +27 followed by 9 digits
        String cellPhoneRegex = "^\\+27[0-9]{9}$";
        return cellPhoneNumber != null && Pattern.matches(cellPhoneRegex, cellPhoneNumber);
    }

    // Seeing if the user passes all the rules to register
    public String registerUser() {
        if (!checkUserName()) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber()) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
        return "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added."; // \n just skips to the next line
    }

    // Checking if they typed the right login info
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return this.username.equals(enteredUsername) && this.password.equals(enteredPassword);
    }

    // Returning the final login message
    public String returnLoginStatus(boolean isLoggedIn) {
        if (isLoggedIn) {
            return "Welcome " + firstName + " " + lastName + " it is great to see you.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}
