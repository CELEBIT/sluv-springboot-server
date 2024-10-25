package com.sluv.admin.router.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class HomeRouter {

    @GetMapping("/home")
    public String getHomePage(Model model) {
        return "home/home";
    }
}
