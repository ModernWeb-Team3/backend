package kr.unideal.server.backend.controller;

import kr.unideal.server.backend.dto.*;
import kr.unideal.server.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/auth/signup")
    public ResponseEntity<SignUpResponseDTO> signup(
            @RequestBody SignUpRequestDTO signUpRequestDTO,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("입력값 규격이 올바르지 않습니다.");
        }

        try {
            userService.register(signUpRequestDTO);
        } catch (IllegalArgumentException e) {
            throw e;
        }

        return ResponseEntity.ok(
                new SignUpResponseDTO()
        );
    }

    // 인증번호 기반 코드 인증 시도
    @PostMapping("/auth/validate")
    public ResponseEntity<VerifyResponseDTO> validate(@RequestBody VerifyRequestDTO verifyRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("입력값 규격이 올바르지 않습니다.");
        }

        try {
            userService.verifyUser(verifyRequestDTO);
        } catch (IllegalArgumentException e) {
            throw e;
        }

        return ResponseEntity.ok(
                new VerifyResponseDTO()
        );
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LogInResponseDTO> login(
            @RequestBody LogInRequestDTO logInRequestDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("입력값 규격이 올바르지 않습니다.");
        }

        try {
            userService.login(logInRequestDTO);
        } catch (IllegalArgumentException e) {
            throw e;
        }

        return ResponseEntity.ok(
                new LogInResponseDTO()
        );
    }
}