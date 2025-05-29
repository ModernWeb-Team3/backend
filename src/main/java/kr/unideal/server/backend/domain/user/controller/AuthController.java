package kr.unideal.server.backend.domain.user.controller;

import jakarta.servlet.http.HttpServletResponse;
import kr.unideal.server.backend.global.util.VerificationCodeUtils;
import kr.unideal.server.backend.domain.user.dto.CustomUserDetails;
import kr.unideal.server.backend.domain.user.dto.request.EmailSendRequestDTO;
import kr.unideal.server.backend.domain.user.dto.request.LogInRequestDTO;
import kr.unideal.server.backend.domain.user.dto.request.SignUpRequestDTO;
import kr.unideal.server.backend.domain.user.dto.request.VerifyRequestDTO;
import kr.unideal.server.backend.domain.user.dto.response.LogInResponseDTO;
import kr.unideal.server.backend.domain.user.service.MailService;
import kr.unideal.server.backend.domain.user.service.UserService;
import kr.unideal.server.backend.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static kr.unideal.server.backend.global.properties.Constants.AUTH_HEADER;
import static kr.unideal.server.backend.global.properties.Constants.REFRESH_TOKEN_SUBJECT;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final MailService mailService;

    // 이메일 인증 코드 전송
    @PostMapping("/auth/email")
    public ApiResponse<String> sendVerificationCode(@RequestBody EmailSendRequestDTO request) {
        String code = VerificationCodeUtils.generateVerificationCode();
        mailService.sendVerificationCode(request.getEmail(), code);
        mailService.saveVerificationCode(request.getEmail(), code);
        return ApiResponse.ok("인증 메일이 전송되었습니다.");
    }


    // 인증 코드 검증
    @PostMapping("/auth/validate")
    public ApiResponse<Boolean> validate(@RequestBody VerifyRequestDTO verifyRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("입력값 규격이 올바르지 않습니다.");
        }

        try {
            boolean verified = userService.verifyUser(verifyRequestDTO);
            return ApiResponse.ok(verified);

        } catch (IllegalArgumentException e) {
            throw e;
        }
    }


    //회원가입
    @PostMapping("/auth/signup")
    public ApiResponse<String> signup(
            @RequestBody SignUpRequestDTO signUpRequestDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("입력값 규격이 올바르지 않습니다.");
        }

        userService.register(signUpRequestDTO);
        return ApiResponse.ok("회원가입이 완료되었습니다.");
    }


    // 로그인
    @PostMapping("/auth/login")
    public ApiResponse<String> login(
            @RequestBody LogInRequestDTO logInRequestDTO,
            BindingResult bindingResult,HttpServletResponse response

    ) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("입력값 규격이 올바르지 않습니다.");
        }


        LogInResponseDTO user = userService.login(logInRequestDTO,response);
        addAccessTokenHeader(user.getJwtToken(),response);
        return ApiResponse.ok("로그인이 성공적으로 완료되었습니다.");


    }

    //로그아웃
    @PostMapping("/auth/logout")
    public ApiResponse<String> logout() {
        userService.logout();
        return ApiResponse.ok("성공적으로 로그아웃 되었습니다.");
    }


    // 회원 탈퇴
    @DeleteMapping("/auth/delete")
    public ApiResponse<String> delete(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.deleteuser(userDetails.getId());
        return ApiResponse.ok("성공적으로 탈퇴되었습니다.");
    }


    // refresh 토큰으로 accessToken, refreshToken을 재발급
    @PostMapping("/auth/reissue")
    public ApiResponse<String> reissue(@RequestHeader(REFRESH_TOKEN_SUBJECT) String refreshToken, HttpServletResponse response) {
        String accessToken = userService.reissue(refreshToken, response);
        addAccessTokenHeader(accessToken, response);
        return ApiResponse.ok("성공적으로 재발급되었습니다.");
    }


    private void addAccessTokenHeader(String accessToken, HttpServletResponse response) {
        response.setHeader(AUTH_HEADER, accessToken);
    }

}
