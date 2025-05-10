package me.alex4386.gachon.sw14856.week10;


import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class OpenAPIController {
    @Value("${springdoc.api-docs.path}")
    private String openApiUrl;

    @GetMapping("/docs")
    @Hidden
    public String apiSpec(Model model) {
        model.addAttribute("openApiUrl", openApiUrl);

        return "scalar";
    }
}