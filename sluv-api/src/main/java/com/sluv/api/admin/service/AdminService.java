package com.sluv.api.admin.service;

import com.sluv.api.admin.dto.request.AdminRequest;
import com.sluv.api.admin.dto.request.AdminUserTokenRequest;
import com.sluv.api.admin.dto.response.AdminResponse;
import com.sluv.api.admin.dto.response.AdminUserTokenResponse;
import com.sluv.api.common.utils.PasswordEncoderUtil;
import com.sluv.domain.admin.service.AdminDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminDomainService adminDomainService;

    @Transactional(readOnly = true)
    public AdminUserTokenResponse getUserTokenByAdmin(AdminUserTokenRequest request) {
        String dbPassword = adminDomainService.getAdminPasswordByEmail(request.getEmail());
        boolean isMatched = PasswordEncoderUtil.matches(request.getPassword(), dbPassword);
        String accessToken = adminDomainService.getUserTokenByAdmin(isMatched, request.getUserId());
        return AdminUserTokenResponse.from(accessToken);
    }

    @Transactional(readOnly = true)
    public AdminResponse getAdminData(AdminRequest request) {
        String encodedPassword = PasswordEncoderUtil.encodePassword(request.getPassword());
        return AdminResponse.of(request.getEmail(), encodedPassword);
    }

}
