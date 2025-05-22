package kr.unideal.server.backend.service;


import kr.unideal.server.backend.config.security.JwtTokenProvider;
import kr.unideal.server.backend.dto.LogInRequestDTO;
import kr.unideal.server.backend.dto.LogInResponseDTO;
import kr.unideal.server.backend.dto.SignUpRequestDTO;
import kr.unideal.server.backend.entity.User;
import kr.unideal.server.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입 db 등록 method
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
        //user.setVerificationToken(UUID.randomUUID().toString());

        userRepository.save(user);
    };

    public LogInResponseDTO loginMember(LogInRequestDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!dto.getPassword().matches(user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }


        //사용자 역할 부여 부분은 구현이 필요없어서 임의로 stirng값 넣어뒀습니다. 필요하면 수정 말해주세요

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(), null,
                Collections.singleton(() -> "ROLE_USER")
        );

        String accessToken = jwtTokenProvider.generateToken(authentication);

        return new LogInResponseDTO(user.getId(), accessToken); // DTO 필드에 맞게
    }

    //에러 났던 코드인데 원인몰라서 다시 짰습니다.ㅠㅠ
//    @Override
//    public LoginResultDTO loginMember(LoginRequestDTO dto) {
//
//        User user = userRepository.findByEmail(dto.getEmail())
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));
//
//        if (!dto.getPassword().matches(user.getPassword())) {
//            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
//        }
//
//
//        Authentication authentication = new UsernamePasswordAuthenticationToken(
//                user.getEmail(), null,
//                Collections.singleton(() -> user.getRole().name())
//        );
//
//        String accessToken = jwtTokenProvider.generateToken(authentication);
//
//        return MemberConverter.toLoginResultDTO(
//                user.getId(),
//                accessToken
//        );
//    }

}
