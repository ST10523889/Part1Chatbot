import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // setting up variables for user details
        String firstName;
        String lastName;
        String username;
        String password;
        String cellPhoneNumber;

        // getting registration info
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

        // making the new user object
        Login user = new Login(username, password, cellPhoneNumber, firstName, lastName);

        // showing if registration worked or failed
        String registrationResult = user.registerUser();
        System.out.println("\n--- Registration Status ---");
        System.out.println(registrationResult);

        // only letting them try log in if reg passed all rules
        if (user.checkUserName() &&
                user.checkPasswordComplexity() &&
                user.checkCellPhoneNumber()) {

            System.out.println("Login user");

            // getting login inputs from the user
            System.out.println("Enter username for login");
            String inputUsername = scanner.nextLine();

            System.out.println("Enter password for login");
            String inputPassword = scanner.nextLine();

            // checking if the entered details match
            boolean isAuthentication = user.loginUser(inputUsername, inputPassword);

            // printing the welcome or error message
            String loginOutput = user.returnLoginStatus(isAuthentication);
            System.out.println("\n--- Login Status ---");
            System.out.println(loginOutput);
        } else {
            // stopping login if registration failed
            System.out.println("\n [!] Registration failed, please try again.");
        }
    }
}