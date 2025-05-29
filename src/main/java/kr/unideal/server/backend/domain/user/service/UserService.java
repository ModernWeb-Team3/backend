package kr.unideal.server.backend.domain.user.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import kr.unideal.server.backend.security.util.CookieUtil;
import kr.unideal.server.backend.security.util.JwtTokenProvider;
import kr.unideal.server.backend.domain.user.dto.CustomUserDetails;
import kr.unideal.server.backend.domain.user.dto.request.LogInRequestDTO;
import kr.unideal.server.backend.domain.user.dto.request.SignUpRequestDTO;
import kr.unideal.server.backend.domain.user.dto.request.VerifyRequestDTO;
import kr.unideal.server.backend.domain.user.dto.response.LogInResponseDTO;
import kr.unideal.server.backend.domain.user.entity.User;
import kr.unideal.server.backend.domain.user.entity.VerificationCode;
import kr.unideal.server.backend.domain.user.repository.EmailRepository;
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

import java.time.LocalDateTime;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ValidatorService validatorService;
    private final CookieUtil cookieUtil;
    private final JwtTokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailRepository emailRepository;
    private final JwtTokenProvider jwtTokenProvider;


    //회원가입 db 등록 method
    public void register(SignUpRequestDTO dto) {

        if (!validatorService.isGachonUnivStudent(dto.getEmail())) {
            throw new IllegalArgumentException("가천대학교 학생 이메일이 아닙니다.");
        }
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        user.setName(dto.getName());
        user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
    }


    //로그인 정보 확인 method
    public LogInResponseDTO login(LogInRequestDTO dto, HttpServletResponse response) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!user.isVerified()) {
            throw new IllegalArgumentException("아직 이메일이 인증되지 않았습니다.");
        }

        if (!bCryptPasswordEncoder.matches(dto.getPassword(), user.getPassword())) {
            userRepository.save(user);
            throw new BadCredentialsException("로그인 실패했습니다.");
        }

        CustomUserDetails userDetails = new CustomUserDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken=tokenProvider.generateRefreshToken(authentication);

        setRefreshTokenCookie(refreshToken, response);

        return LogInResponseDTO.from(user.getEmail(), accessToken);
    }

    // 인증
    @Transactional
    public boolean verifyUser(VerifyRequestDTO dto) {
        // 1. 인증 코드 유효성 확인
        VerificationCode code = emailRepository.findByEmailAndCode(dto.getEmail(), dto.getCode())
                .filter(vc -> vc.getExpiresTime().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않거나 만료된 인증 코드입니다."));

        // 2. 유저 조회 또는 생성
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(dto.getEmail());
                    newUser.setVerified(false); // 최초 생성 시 미인증
                    return userRepository.save(newUser);
                });

        // 3. 이미 인증된 유저인지 확인
        if (user.isVerified()) {
            throw new IllegalArgumentException("이미 인증된 사용자입니다.");
        }

        // 4. 인증 처리
        user.setVerified(true);

        // 5. 인증 코드 삭제
        emailRepository.delete(code);

        return true;
    }


    // 인증토큰 발급
//    public void issueVerificationCode(User user) {
//        String verificationCode = VerificationCodeUtils.generateVerificationCode();
//
//        user.setVerificationToken(verificationCode);
//        mailService.sendVerificationCode(user.getEmail(), verificationCode);
//    }

    // 로그아웃
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

    //회원 탈퇴
    public void deleteuser(Long id) {
    }

    /// refreshToken 재발급
    public String reissue(String refreshToken, HttpServletResponse response) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token입니다.");
        }

        // Refresh Token에서 사용자 정보 추출
        Authentication authentication = tokenProvider.getAuthentication(refreshToken);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // 새로운 Access Token 발급
        String newAccessToken = tokenProvider.generateAccessToken(authentication);

        // 새로운 Refresh Token 발급
        String newRefreshToken = tokenProvider.generateRefreshToken(authentication);

        // 쿠키에 새로 발급한 Refresh Token 설정
        setRefreshTokenCookie(newRefreshToken, response);

        log.info("🔄 Refresh Token 재발급 완료 - userId: {}", userDetails.getId());
        return newAccessToken;
    }

    // 쿠키에 RefreshToken 설정 (HttpServletResponse 필요)
    public void setRefreshTokenCookie(String refreshToken, HttpServletResponse response) {
        cookieUtil.setCookie(refreshToken, response);
        log.info("🍪 쿠키에 RefreshToken 저장 완료 - key: {}", refreshToken);
    }


    // 유저 검색
    public User loaduser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 멤버가 존재하지 않습니다."));

    }
}
