package com.sluv.admin.common.service;

import com.sluv.domain.admin.entity.Admin;
import com.sluv.domain.admin.service.AdminDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AdminDomainService adminDomainService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = adminDomainService.findByEmail(email);
        return toUserDetails(admin);
    }

    private UserDetails toUserDetails(Admin admin) {
        return User.builder()
                .username(admin.getEmail())
                .password(admin.getPwd())
                .authorities(new SimpleGrantedAuthority("USER"))
                .build();
    }
}