package kr.unideal.server.backend.controller;

import kr.unideal.server.backend.dto.LogInRequestDTO;
import kr.unideal.server.backend.dto.SignUpRequestDTO;
import kr.unideal.server.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// 대체 왜 여기에 SSR 을????
// 일단 API는 RestController 사용하는 것이 편하므로 분리
//
// 아니 근데 FE React 로 만들기로 한거 아녔나.... Thymeleaf 로 왜...?
@Controller
@RequiredArgsConstructor
public class AuthPageController {

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

}