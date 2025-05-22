package kr.unideal.server.backend.utils;

import java.security.SecureRandom;

public class VerificationCodeUtils {
    static String verificationCodeCharacters = "0123456789";
    static int verificationCodeLength = 6;

    public static String generateVerificationCode() {
        // use system-wide entropy to make sure that
        // verification code can not be predicted via
        // PRNG seed prediction
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < verificationCodeLength; i++) {
            int idx = random.nextInt(verificationCodeCharacters.length());
            builder.append(verificationCodeCharacters.charAt(idx));
        }

        return builder.toString();
    }
}
