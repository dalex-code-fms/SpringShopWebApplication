package fr.fms.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logoutConfirm")
    public String logoutConfirm() {
        return "logoutConfirm";
    }
}
