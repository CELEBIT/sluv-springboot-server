package com.sluv.server.domain.user.service;

import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User test(){

        return User.builder()
                .email("gi")
                .build();
    }
}
