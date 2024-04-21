package auth;

import utility.PasswordHasher;

import java.io.*;
import java.util.HashMap;

public class AuthDB {
    private final HashMap<String,User> userDB = new HashMap<>();
    private final PasswordHasher hash = new PasswordHasher();
    private final String userStorageFileName = "/Users/Sumit/Desktop/b00955671_sumitsavaliya_a1/src/main/java/persistentStorage/userdata.txt";

    /**
     * Initializes the Authentication Database by loading user data from a text file.
     * <p>
     * This constructor is responsible for initializing the Authentication Database
     * by reading user data from a text file. It opens the specified text file and
     * populates the user database with existing user records. Each line in the text file
     * should contain a username and a hashed password separated by a comma.
     * <p>
     * If the file exists and contains valid user data, the user database will be
     * initialized with these users for authentication purposes.
     *
     */
    public AuthDB() {
        try (BufferedReader br = new BufferedReader(new FileReader(userStorageFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    userDB.put(parts[0], new User(parts[0], parts[1]));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Adds a new user to the User Database and saves the changes in a text file.
     * <p>
     * This method allows users to sign up for the application by providing a username
     * and a password. The provided password is securely hashed before storage. After
     * adding the new user to the database, the method ensures that the user data is saved
     * to a text file.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     */
    public void addUser(String username , String password) {
        userDB.put(username , new User(username , hash.hashWithMD5(password)));
        saveUsers();
    }

    /**
     * Retrieves a user from the User Database based on the provided username.
     * <p>
     * This method searches for a user with the specified username in the User Database.
     * If a user with the given username exists in the database, the method returns
     * the corresponding User object. If no user is found, the method returns null.
     *
     * @param username The username of the user to retrieve.
     * @return The User object associated with the provided username, or null if no user is found.
     */
    public User getUser(String username) {
        return userDB.get(username);
    }

    /**
     * Saves user data to a text file for persistent storage.
     * <p>
     * This method iterates through the user database and writes user information, including
     * usernames and hashed passwords, to a text file. The stored data is essential for maintaining
     * user records between application sessions.
     *
     * The data is stored in a comma-separated format, with each line representing a user's data.
     */
    private void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(userStorageFileName))) {
            for (User user : userDB.values()) {
                bw.write(user.getUsername() + "," + user.getPassword());
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
