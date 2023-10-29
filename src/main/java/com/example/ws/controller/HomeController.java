package com.example.ws.controller;

import com.example.ws.dto.security.PrincipalDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal PrincipalDetails details) {

        return "index";
    }

    @GetMapping("/test/ad")
    public String asdqd() {
        return "admin/stadiumRegister";
    }
}
