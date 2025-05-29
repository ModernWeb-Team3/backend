package kr.unideal.server.backend.global.util;

import java.security.SecureRandom;

public class VerificationCodeUtils {
    static String verificationCodeCharacters = "0123456789";
    static int verificationCodeLength = 6;

    // 난수 생성
    public static String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < verificationCodeLength; i++) {
            int idx = random.nextInt(verificationCodeCharacters.length());
            builder.append(verificationCodeCharacters.charAt(idx));
        }

        return builder.toString();
    }
}
