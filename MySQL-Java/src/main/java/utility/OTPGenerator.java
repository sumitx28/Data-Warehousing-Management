package utility;

import java.util.Random;

public class OTPGenerator {

    /**
     * Generates a random CAPTCHA/OTP.
     * <p>
     * This method generates a random CAPTCHA to be used for user authentication purpose.
     *
     * @return A randomly generated CAPTCHA as a formatted string.
     */
    public String generateRandomCaptcha() {
        Random random = new Random();
        int captchaNumber = random.nextInt(100000);
        return String.format("%04d", captchaNumber);
    }
}
