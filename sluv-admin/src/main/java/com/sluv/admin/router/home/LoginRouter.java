package com.sluv.admin.router.home;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class LoginRouter {

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "home/login";
    }

}
