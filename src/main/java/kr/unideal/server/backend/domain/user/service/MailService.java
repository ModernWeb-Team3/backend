package kr.unideal.server.backend.domain.user.service;

import kr.unideal.server.backend.domain.user.entity.VerificationCode;
import kr.unideal.server.backend.domain.user.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${unideal.mailer.from}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;
    private final EmailRepository emailRepository;

    // 이메일 인증 메일 전송-1
    public void sendSimpleMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    // 이메일 인증 메일 전송-2
    public void sendVerificationCode(String to, String code) {

        this.sendSimpleMail(
                to,
                "Unideal 가입 인증코드",
                "Unideal에 가입해 주셔서 감사합니다. 회원가입을 위한 인증코드는 "+code+" 입니다."
        );
    }

    // 이메일 인증 코드 저장
    public void saveVerificationCode(String email, String code) {
        VerificationCode verificationCode = VerificationCode.of(email, code, LocalDateTime.now().plusMinutes(10));
        emailRepository.save(verificationCode);
    }


    @Scheduled(cron = "0 0 12 * * *") // 매일 정오(12:00 PM) 실행
    public void deleteExpiredCodes() {
        emailRepository.deleteByExpiresTimeBefore(LocalDateTime.now());
    }


}
