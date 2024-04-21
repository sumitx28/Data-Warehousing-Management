package utility;

import java.security.MessageDigest;

public class PasswordHasher {

    /**
     * Hashes a password using the MD5 (Message Digest 5) algorithm.
     * <p>
     * This method takes a password as input and securely hashes it using the MD5 algorithm.
     *
     * @param password The password to be hashed.
     * @return The MD5 hash of the provided password as a hexadecimal string, or null in case of an error.
     */
    public String hashWithMD5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(password.getBytes());

            StringBuilder result = new StringBuilder();
            for (byte b : digest) {
                result.append(String.format("%02x", b));
            }

            return result.toString();

        }catch(Exception e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * Verifies whether an entered password matches a stored (hashed) password.
     * <p>
     * This method compares the entered password, after hashing it with MD5, with a stored hashed password.
     * It is used to verify the authenticity of a user during the login process.
     *
     * @param enteredString The entered password to be verified.
     * @param storedPass The stored (hashed) password for comparison.
     * @return true if the entered password matches the stored password; false otherwise.
     */
    public boolean verifyPassword(String enteredString, String storedPass) {
        return hashWithMD5(enteredString).equals(storedPass);
    }
}
