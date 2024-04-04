package com.sluv.server.domain.admin.controller;

import com.sluv.server.domain.admin.entity.Admin;
import com.sluv.server.domain.admin.repository.AdminRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.discord.WebHookService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AdminRepository adminRepository;
    private final WebHookService webHookService;

    @GetMapping("/admin")
    public void test() {

        adminRepository.save(Admin.builder()
                .id(2L)
                .pwd("4321")
                .email("junker@gmail.com")
                .name("junker")
                .build());
    }

    @GetMapping("/webhook")
    public void test(@AuthenticationPrincipal User user) {
        webHookService.sendSingupMessage(user);
        webHookService.sendWithdrawMessage(user);
    }


}
