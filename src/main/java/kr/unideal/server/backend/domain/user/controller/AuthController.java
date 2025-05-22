package kr.unideal.server.backend.domain.user.controller;

import jakarta.mail.MessagingException;
import kr.unideal.server.backend.domain.user.dto.request.EmailSendRequest;
import kr.unideal.server.backend.domain.user.dto.request.EmailVerifyRequest;
import kr.unideal.server.backend.domain.user.dto.request.LoginRequest;
import kr.unideal.server.backend.domain.user.dto.response.LoginResponse;
import kr.unideal.server.backend.domain.user.exception.NotVerifiedException;
import kr.unideal.server.backend.domain.user.service.EmailService;
import kr.unideal.server.backend.domain.user.service.UserService;
import kr.unideal.server.backend.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final EmailService emailService;

    // 이메일 전송하기
    @PostMapping("/email")
    public ApiResponse<String> email(@RequestBody EmailSendRequest request) throws MessagingException {
        emailService.sendEmail(request.getEmail());
        return ApiResponse.ok("성공적으로 메일이 전송되었습니다.");
    }

    // 이메일 검증하기
    @PostMapping("/email/verify")
    public ApiResponse<String> verify(@RequestBody EmailVerifyRequest request) {
        if (!emailService.verifyCode(request.getEmail(), request.getCode())){
            throw new NotVerifiedException("인증번호가 틀렸습니다.");
        }

        return ApiResponse.ok("메일이 인증되었습니다.");
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        return ApiResponse.ok(userService.login(request));
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout() {
        userService.logout();
        return ApiResponse.ok("성공적으로 로그아웃 되었습니다.");
    }


//    @DeleteMapping()
//    public ApiResponse<String> delete(@AuthenticationPrincipal CustomUserDetails userDetails) {
//        userService.deleteuser(userDetails.getId());
//        return ApiResponse.ok("성공적으로 탈퇴되었습니다.");
//    }

//    @PostMapping("/request")
//    public ApiResponse<String> requestReset(@AuthenticationPrincipal CustomUserDetails userDetails) throws MessagingException {
//        userService.requestPasswordChange(userDetails.getId());
//        return ApiResponse.ok("성공적으로 비밀번호 요청메일이 전송되었습니다.");
//
//    }

//    @PutMapping("/reset-password")
//    public ApiResponse<String> resetPassword(@RequestParam String token, @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody userPasswordChangeRequest request) {
//
//        userService.changePassword(token, userDetails.getId(), request.getNewPassword(), request.getConfirmPassword());
//        return ApiResponse.ok("성공적으로 비밀번호가 변경되었습니다.");
//    }

}
