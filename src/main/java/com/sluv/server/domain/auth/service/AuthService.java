package com.sluv.server.domain.auth.service;

import com.sluv.server.domain.auth.dto.AuthResponseDto;
import com.sluv.server.domain.auth.dto.SocialUserInfoDto;
import com.sluv.server.domain.auth.enums.SnsType;
import com.sluv.server.domain.closet.service.ClosetService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.exception.UserNoFCMException;
import com.sluv.server.domain.user.exception.UserNotFoundException;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    private final ClosetService closetService;

    public AuthResponseDto getAuthResDto(User user) {

        return AuthResponseDto.builder()
                .token(jwtProvider.createAccessToken(user))
                .userStatus(user.getUserStatus())
                .build();
    }

    public User getOrCreateUser(SocialUserInfoDto userInfoDto, SnsType snsType, String fcm) {
        User user = userRepository.findByEmail(userInfoDto.getEmail()).orElse(null);

        if (user == null) {
            user = userRepository.save(User.toEntity(userInfoDto, snsType, fcm));

            // 생성과 동시에 기본 Closet 생성
            closetService.postBasicCloset(user);
        }

        changeFcm(user.getId(), fcm);

        return user;
    }

    public void checkFcm(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (user.getFcmToken() == null) {
            throw new UserNoFCMException();
        }
    }

    public void changeFcm(Long userId, String fcmToken) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.changeFcmToken(fcmToken);
        userRepository.save(user);
    }
}
