package kr.unideal.server.backend.domain.user.service;

import jakarta.persistence.EntityNotFoundException;
import kr.unideal.server.backend.config.security.JwtTokenProvider;
import kr.unideal.server.backend.domain.user.dto.CustomUserDetails;
import kr.unideal.server.backend.domain.user.dto.request.LoginRequest;
import kr.unideal.server.backend.domain.user.dto.request.SignUpRequest;
import kr.unideal.server.backend.domain.user.dto.response.LoginResponse;
import kr.unideal.server.backend.domain.user.entity.User;
import kr.unideal.server.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final EmailService emailService;


    // 회원가입 db 등록 method
    public void register(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        User user =User.of(request);
        user.setVerified(true);
        //user.setVerificationToken(UUID.randomUUID().toString());

        userRepository.save(user);
    }

    public LoginResponse login(@RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("해당하는 이메일을 사용하는 유저가 없습니다."));

        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            userRepository.save(user);
            throw new BadCredentialsException("로그인 실패했습니다.");
        }

        CustomUserDetails userDetails = new CustomUserDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateAccessToken(authentication);

        return LoginResponse.from(user.getEmail(), token);
    }

    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getId(); // 현재 사용자 ID 가져오기

            // 현재 인증된 사용자 정보 삭제
            SecurityContextHolder.clearContext();

            log.info("✅ 로그아웃 완료 - userId: {}", userId);
        } else {
            log.warn("⚠️ 로그아웃 실패: 인증 정보 없음");
        }
    }

//
//    public CustomUserDetails loadMyuser(Long userId) {
//        User user = repository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("해당하는 멤버가 존재하지 않습니다."));
//
//        return userDetailResponse.from(user);
//
//    }


}
