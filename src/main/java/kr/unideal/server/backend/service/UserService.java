package kr.unideal.server.backend.service;


import kr.unideal.server.backend.dto.SignUpRequestDTO;
import kr.unideal.server.backend.entity.User;
import kr.unideal.server.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public void register(SignUpRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        System.out.println("이메일: " + dto.getEmail());
        System.out.println("비밀번호: " + dto.getPassword());
        System.out.println("이름: " + dto.getName());

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setName(dto.getName());

        //이메일 인증이 완료 되어야 회원가입을 진행할 예정이라 일단 verified = TRUE 로 해둠
        user.setVerified(true);
//        user.setVerificationToken(UUID.randomUUID().toString());

        userRepository.save(user);
    }
}
