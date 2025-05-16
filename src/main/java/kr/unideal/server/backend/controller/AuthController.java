package kr.unideal.server.backend.controller;

import jakarta.validation.Valid;
import kr.unideal.server.backend.dto.SignUpRequestDTO;
import kr.unideal.server.backend.service.UserService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("logInRequestDTO", new L)
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
    }
}