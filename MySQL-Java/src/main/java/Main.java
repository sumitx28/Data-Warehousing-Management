import auth.User;
import database.QueryProcessor;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CaptchaAuth auth = new CaptchaAuth();
        User currentUser = auth.start();

        if (currentUser != null) {
            QueryProcessor queryProcessor = new QueryProcessor();
            Scanner scanner = new Scanner(System.in);

            boolean running = true;
            while (running) {
                System.out.print("Enter a SQL query (or 'exit' to quit): ");
                String userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("exit")) {
                    running = false;
                } else {
                    String result = queryProcessor.executeQuery(userInput);
                    System.out.println("Query Result: " + result);
                }
            }

            scanner.close();
        }
    }
}
