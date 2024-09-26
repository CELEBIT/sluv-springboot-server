package com.sluv.api.auth.service;

import com.sluv.api.auth.dto.response.AuthResponse;
import com.sluv.api.closet.service.ClosetService;
import com.sluv.domain.auth.dto.SocialUserInfoDto;
import com.sluv.domain.auth.enums.SnsType;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.exception.UserNoFCMException;
import com.sluv.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    //    private final JwtProvider jwtProvider;
    private final UserDomainService userDomainService;

    private final ClosetService closetService;

    public AuthResponse getAuthResDto(User user) {
//        return AuthResponse.of(jwtProvider.createAccessToken(user.getId()), user.getUserStatus());
        return null;
    }

    public User getOrCreateUser(SocialUserInfoDto userInfoDto, SnsType snsType, String fcm) {
        User user = userDomainService.findByEmailOrNull(userInfoDto.getEmail());

        if (user == null) {
            user = userDomainService.createUser(User.toEntity(userInfoDto, snsType, fcm));

            // 생성과 동시에 기본 Closet 생성
            closetService.postBasicCloset(user);
        }

        changeFcm(user.getId(), fcm);

        return user;
    }

    public void checkFcm(User user) {
        if (user.getFcmToken() == null) {
            throw new UserNoFCMException();
        }
    }

    public void changeFcm(Long userId, String fcmToken) {
        User user = userDomainService.findById(userId);
        user.changeFcmToken(fcmToken);
        userDomainService.saveUser(user);
    }

    public User findLogInUser(Long userId) {
        return userDomainService.findById(userId);
    }

}
