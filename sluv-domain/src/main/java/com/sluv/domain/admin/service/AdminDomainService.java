package com.sluv.domain.admin.service;

//import com.sluv.common.jwt.JwtProvider;

import com.sluv.domain.admin.entity.Admin;
import com.sluv.domain.admin.exception.AdminNotFoundException;
import com.sluv.domain.admin.exception.AdminPasswordNotMatchException;
import com.sluv.domain.admin.repository.AdminRepository;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.exception.UserNotFoundException;
import com.sluv.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDomainService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

//    private final JwtProvider jwtProvider;

    public String getAdminPasswordByEmail(String email) {
        Admin admin = adminRepository.findByEmail(email).orElseThrow(AdminNotFoundException::new);
        return admin.getPwd();
    }

    public String getUserTokenByAdmin(boolean isMatched, Long userId) {
        if (!isMatched) {
            throw new AdminPasswordNotMatchException();
        }
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
//        return jwtProvider.createAccessToken(user.getId());
        return null;
    }

    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email).orElseThrow(AdminNotFoundException::new);
    }
}
