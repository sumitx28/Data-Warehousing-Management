package auth;

public class User {
    private final String username;
    private final String password;

    /**
     * Constructs a new User object with the specified username and password.
     * <p>
     * This constructor creates a User object with the provided username and password.
     * User objects represent users of the authentication system, and they are used
     * to store user data, including their login credentials.
     *
     * @param username The username of the user.
     * @param password The hashed password of the user for secure authentication.
     */
    public User(String username , String password){
        this.username = username;
        this.password = password;
    }

    /**
     * Retrieves the user's hashed password.
     * <p>
     * This method returns the hashed password associated with the user.
     * The password is securely hashed for authentication and should not be stored in plain text.
     *
     * @return The hashed password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Retrieves the username of the user.
     * <p>
     * This method returns the username of the user. Usernames are used for user identification
     * and authentication within the system.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }
}
