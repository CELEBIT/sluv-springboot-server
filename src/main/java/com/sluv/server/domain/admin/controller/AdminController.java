package com.sluv.server.domain.admin.controller;

import com.sluv.server.domain.admin.dto.AdminReqDto;
import com.sluv.server.domain.admin.dto.AdminResDto;
import com.sluv.server.domain.admin.dto.AdminUserTokenReqDto;
import com.sluv.server.domain.admin.dto.AdminUserTokenResDto;
import com.sluv.server.domain.admin.service.AdminService;
import com.sluv.server.global.common.response.SuccessDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/token")
    public ResponseEntity<SuccessDataResponse<AdminUserTokenResDto>> getUserTokenByAdmin(
            @RequestBody AdminUserTokenReqDto dto) {

        return ResponseEntity.ok().body(SuccessDataResponse.<AdminUserTokenResDto>builder()
                .result(adminService.getUserTokenByAdmin(dto.getEmail(), dto.getPassword(), dto.getUserId()))
                .build()
        );
    }

    @PostMapping("")
    public ResponseEntity<SuccessDataResponse<AdminResDto>> saveAdmin(
            @RequestBody AdminReqDto dto) {

        return ResponseEntity.ok().body(SuccessDataResponse.<AdminResDto>builder()
                .result(adminService.getAdminData(dto.getEmail(), dto.getPassword()))
                .build()
        );
    }

}
