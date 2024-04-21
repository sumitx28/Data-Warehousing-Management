import auth.AuthDB;
import auth.User;
import utility.OTPGenerator;
import utility.PasswordHasher;

import java.util.Scanner;

public class CaptchaAuth {
    Scanner in = new Scanner(System.in);
    AuthDB auth = new AuthDB();
    PasswordHasher hash = new PasswordHasher();
    OTPGenerator gen = new OTPGenerator();

    /**
     * Initiates user authentication or registration flow.
     * <p>
     * This method handles the user login or sign-up process. It continuously prompts the user
     * for login or sign-up choices and collects user credentials (username and password).
     * If the user chooses to log in, a CAPTCHA (One-Time Password) is generated and presented.
     * The user must enter the correct CAPTCHA to proceed with login. After successful login,
     * the associated User object is returned. For sign-up, a new user is created and stored in the database.
     *
     * @return The User object representing the authenticated user or null if the process is interrupted.
     */
    public User start() {
        while(true) {
            String username;
            String password;
            String otp = gen.generateRandomCaptcha();

            System.out.println("Hello User. This is your LIGHT WEIGHT DBMS");
            System.out.println("-------------------------------------------");

            System.out.println("Please Login or Sign up");
            System.out.println("------------------------------------");
            System.out.println("Enter 1 to Login and 2 to Sign up");

            int choice = in.nextInt();

            System.out.println("Please enter your credentials");
            System.out.println("=============================");
            System.out.print("Username: ");
            username = in.next();

            System.out.print("Password: ");
            password = in.next();

            if(choice == 1){
                System.out.print("CAPTCHA: ");
                System.out.println(otp);

                System.out.println("Enter the OTP: ");
                String captcha = in.next();

                if(!captcha.equals(otp)) {
                    System.out.println("INVALID CAPTCHA! TRY LOGGING IN AGAIN!!");
                    continue;
                }

                User user = auth.getUser(username);

                if(user != null && hash.verifyPassword(password , user.getPassword())){
                    System.out.println("Welcome " + user.getUsername());
                    return user;
                }
                else{
                    System.out.println("Incorrect Credentials. Please try again!");
                }

            }
            else{
                auth.addUser(username,password);
                System.out.println("Sign up successful. Please login!");
            }

        }
    }
}
