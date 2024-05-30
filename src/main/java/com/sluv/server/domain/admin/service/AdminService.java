package com.sluv.server.domain.admin.service;

import com.sluv.server.domain.admin.dto.AdminResDto;
import com.sluv.server.domain.admin.dto.AdminUserTokenResDto;
import com.sluv.server.domain.admin.entity.Admin;
import com.sluv.server.domain.admin.exception.AdminNotFoundException;
import com.sluv.server.domain.admin.exception.AdminPasswordNotMatchException;
import com.sluv.server.domain.admin.repository.AdminRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.exception.UserNotFoundException;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.common.utils.PasswordEncoderUtil;
import com.sluv.server.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;
    private final PasswordEncoderUtil passwordEncoderUtil;

    @Transactional(readOnly = true)
    public AdminUserTokenResDto getUserTokenByAdmin(String email, String password, Long userId) {
        Admin admin = adminRepository.findByEmail(email).orElseThrow(AdminNotFoundException::new);
        System.out.println(password);
        boolean isMatched = passwordEncoderUtil.matches(password, admin.getPwd());

        System.out.println(isMatched);
        if (!isMatched) {
            throw new AdminPasswordNotMatchException();
        }

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        String accessToken = jwtProvider.createAccessToken(user);

        return AdminUserTokenResDto.from(accessToken);
    }

    public AdminResDto getAdminData(String email, String password) {
        String encodedPassword = passwordEncoderUtil.encodePassword(password);
        return AdminResDto.of(email, encodedPassword);
    }
}
