package com.sluv.api.admin.controller;

import com.sluv.api.admin.dto.request.AdminRequest;
import com.sluv.api.admin.dto.request.AdminUserTokenRequest;
import com.sluv.api.admin.dto.response.AdminResponse;
import com.sluv.api.admin.dto.response.AdminUserTokenResponse;
import com.sluv.api.admin.service.AdminService;
import com.sluv.api.common.response.SuccessDataResponse;
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
    public ResponseEntity<SuccessDataResponse<AdminUserTokenResponse>> getUserTokenByAdmin(
            @RequestBody AdminUserTokenRequest request) {

        AdminUserTokenResponse response = adminService.getUserTokenByAdmin(request);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

    @PostMapping("")
    public ResponseEntity<SuccessDataResponse<AdminResponse>> saveAdmin(@RequestBody AdminRequest request) {
        AdminResponse response = adminService.getAdminData(request);
        return ResponseEntity.ok().body(SuccessDataResponse.from(response));
    }

}
