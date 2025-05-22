package kr.unideal.server.backend.service;


import kr.unideal.server.backend.dto.LogInRequestDTO;
import kr.unideal.server.backend.dto.SignUpRequestDTO;
import kr.unideal.server.backend.dto.VerifyRequestDTO;
import kr.unideal.server.backend.entity.User;
import kr.unideal.server.backend.repository.UserRepository;
import kr.unideal.server.backend.utils.VerificationCodeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MailService mailService;
    private final ValidatorService validatorService;

    private PasswordEncoder passwordEncoder;

    //회원가입 db 등록 method
    public void register(SignUpRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        if (!validatorService.isGachonUnivStudent(dto.getEmail())) {
            throw new IllegalArgumentException("가천대학교 학생 이메일이 아닙니다.");
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());

        // HASH FIRST - PlainText password in database is security nightmare
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        //이메일 인증이 완료 되어야 회원가입을 진행할 예정이라 일단 verified = TRUE 로 해둠
        this.issueVerificationCode(user);
        user.setVerified(false);

        userRepository.save(user);
    };

    //로그인 정보 확인 method
    public User login(LogInRequestDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!user.isVerified()) {
            throw new IllegalArgumentException("아직 이메일이 인증되지 않았습니다.");
        }

        // bcrypt implementation
        String plainText = dto.getPassword();
        String bcrypt = user.getPassword();

        if (!passwordEncoder.matches(plainText, bcrypt)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    };

    // 인증
    public boolean verifyUser(VerifyRequestDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일이 올바르지 않습니다."));

        if (user.isVerified()) throw new IllegalArgumentException("이미 인증된 사용자입니다.");

        boolean result = this.verifyUser(user, dto.getCode());
        if (result) {
            user.setVerified(true);
        }

        return result;
    }

    private boolean verifyUser(User user, String verificationCode) {
        if (user.isVerified()) return true;

        String verificationToken = user.getVerificationToken();
        if (verificationToken == null) {
            return false;
        } else return verificationToken.equals(verificationCode);
    }

    // 인증토큰 발급
    public void issueVerificationCode(User user) {
        String verificationCode = VerificationCodeUtils.generateVerificationCode();

        user.setVerificationToken(verificationCode);
        mailService.sendVerificationCode(user.getEmail(), verificationCode);
    }
}
