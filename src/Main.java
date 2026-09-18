import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Setting up variables for user details
        String firstName;
        String lastName;
        String username;
        String password;
        String cellPhoneNumber;

        // Getting registration info
        System.out.println("Registration");

        System.out.println("Enter your first name: ");
        firstName = scanner.nextLine();

        System.out.println("Enter your last name: ");
        lastName = scanner.nextLine();

        System.out.println("Enter your username: ");
        username = scanner.nextLine();

        System.out.println("Enter your password: ");
        password = scanner.nextLine();

        System.out.println("Enter your phone number: ");
        cellPhoneNumber = scanner.nextLine();

        // Making the new user object
        Login user = new Login(username, password, cellPhoneNumber, firstName, lastName);

        // Showing if registration worked or failed
        String registrationResult = user.registerUser();
        System.out.println("\n--- Registration Status ---");
        System.out.println(registrationResult);

        // Only letting them try log in if reg passed all rules
        if (user.checkUserName() &&
                user.checkPasswordComplexity() &&
                user.checkCellPhoneNumber()) {

            System.out.println("Login user");

            // Getting login inputs from the user
            System.out.println("Enter username for login");
            String inputUsername = scanner.nextLine();

            System.out.println("Enter password for login");
            String inputPassword = scanner.nextLine();

            // Checking if the entered details match
            boolean isAuthentication = user.loginUser(inputUsername, inputPassword);

            // Printing the welcome or error message
            String loginOutput = user.returnLoginStatus(isAuthentication);
            System.out.println("\n--- Login Status ---");
            System.out.println(loginOutput);
        } else {
            // Stopping login if registration failed
            System.out.println("\n [!] Registration failed, please try again.");
        }
    }
}