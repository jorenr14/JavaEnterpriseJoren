package be.ucll.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class AuthController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @PostMapping("/authenticate")
    public String authenticate(@RequestParam String username, @RequestParam String password) {

        return "redirect:/search";

    }
}
