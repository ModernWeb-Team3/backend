package kr.unideal.server.backend.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.unideal.server.backend.dto.LogInRequestDTO;
import kr.unideal.server.backend.dto.LogInResponseDTO;
import kr.unideal.server.backend.dto.SignUpRequestDTO;
import kr.unideal.server.backend.entity.User;
import kr.unideal.server.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    //signup 페이지 GET
    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("signUpRequestDTO", new SignUpRequestDTO());
        return "signup";
    }


    //login 페이지 Get
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("logInRequestDTO", new LogInRequestDTO());
        return "login";
    }

    @PostMapping("/auth/signup")
    public String signup(@Valid @ModelAttribute SignUpRequestDTO signUpRequestDTO,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "입력값을 다시 확인해주세요.");
            return "signup";
        }

        try {
            userService.register(signUpRequestDTO);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }

        return "redirect:/login";
    };

    @PostMapping("/auth/login")
    public ResponseEntity<LogInResponseDTO> login(@RequestBody @Valid LogInRequestDTO loginRequestDTO) {
        try {
            LogInResponseDTO response = userService.loginMember(loginRequestDTO);
            return ResponseEntity.ok(response); // ✅ 토큰 포함 응답
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // 또는 에러 메시지 반환
        }
    }

//    @PostMapping("/auth/login")
//    public String login(@ModelAttribute @Valid LogInRequestDTO loginRequestDTO,
//                        BindingResult bindingResult,
//                        Model model,
//                        HttpSession session) {
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("error", "입력값을 다시 확인해주세요.");
//            return "login";
//        }
//
//        try {
//            User user = userService.login(loginRequestDTO);
//            session.setAttribute("user", user); // 로그인 상태 저장
//            return "redirect:/"; // 홈 또는 마이페이지 등으로 이동
//        } catch (IllegalArgumentException e) {
//            model.addAttribute("error", e.getMessage());
//            return "login";
//        }
//    }
}