package kr.unideal.server.backend.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class ValidatorService {

    public boolean isGachonUnivStudent(String email) {
        String[] splitted = email.toLowerCase().split("@");
        String hostname = splitted[splitted.length - 1];

        if (hostname.endsWith("gachon.ac.kr")) {
            // Gachon University hostname
            return true;
        }

        return false;
    }
}
