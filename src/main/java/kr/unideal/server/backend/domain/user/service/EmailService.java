package kr.unideal.server.backend.domain.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kr.unideal.server.backend.domain.user.entity.VerificationCode;
import kr.unideal.server.backend.domain.user.exception.NotVerifiedException;
import kr.unideal.server.backend.domain.user.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final EmailRepository emailRepository;


    public void sendEmail(String toEmail) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // 기초 세팅
        String title = "UNIDEAL 이메일 인증 번호";
        String code = createVerificationCode(toEmail); // 인증 코드 생성

        // 설정
        helper.setTo(toEmail);
        helper.setSubject(title);

        // 이메일 본문
        String content = """
                    <html>
                    <body style="font-family: Arial, sans-serif; text-align: center;">
                        <h1 style="color: #4285F4;"> UNIDEAL [가천대 전용 중고거래 플랫폼] </h1>
                        <h2 style="color: #4CAF50;">이메일 인증 코드</h2>
                        <p> 회원가입을 위한 인증 코드입니다. 아래의 코드를 입력하여 이메일 인증을 완료하세요.</p>
                        <div style="font-size: 24px; font-weight: bold; background: #f4f4f4; padding: 10px; display: inline-block; border-radius: 5px;">
                            %s
                        </div>
                        <p style="margin-top: 20px;">UNIDEAL에서 가천대 학생들과 거래하세요~!</p>
                        <p style="color: #666; font-size: 12px;">UNIDEAL</p>
                    </body>
                    </html>
                """.formatted(code); // 코드 삽입

        helper.setText(content, true);

        try {
            emailSender.send(message);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new MessagingException("메시지 전송에 실패했습니다.", e);
        }
    }


    // 인증 코드 생성 및 저장
    private String createVerificationCode(String email) {
        String randomCode = generateRandomCode(6);
        VerificationCode code = VerificationCode.builder()
                .email(email)
                .code(randomCode) // 랜덤 코드 생성
                .expiresTime(LocalDateTime.now().plusMinutes(30)) // 30분 후 만료
                .build();

        return emailRepository.save(code).getCode();
    }


    private String generateRandomCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijkLmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    public boolean verifyCode(String email, String code) {
        return emailRepository.findByEmailAndCode(email, code)
                .filter(vc -> vc.getExpiresTime().isAfter(LocalDateTime.now())) // 유효성 검사
                .map(vc -> {
                    emailRepository.delete(vc); // 검증 후 삭제
                    return true;
                })
                .orElseThrow(() -> new NotVerifiedException("유효하지 않거나 만료된 코드입니다."));
    }

    @Scheduled(cron = "0 0 12 * * *") // 매일 정오(12:00 PM) 실행
    public void deleteExpiredCodes() {
        emailRepository.deleteByExpiresTimeBefore(LocalDateTime.now());
    }


}
