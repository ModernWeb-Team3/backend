package kr.unideal.server.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Value("${unideal.mailer.from}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    public void sendVerificationCode(String to, String code) {
        this.sendSimpleMail(
                to,
                "Unideal 가입 인증코드",
                "Unideal에 가입해 주셔서 감사합니다. 회원가입을 위한 인증코드는 "+code+" 입니다."
        );
    }
}