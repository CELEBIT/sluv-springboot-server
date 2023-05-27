package com.sluv.server.domain.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.sluv.server.domain.auth.dto.AuthRequestDto;
import com.sluv.server.domain.auth.dto.AuthResponseDto;
import com.sluv.server.domain.auth.dto.SocialUserInfoDto;
import com.sluv.server.domain.closet.service.ClosetService;
import com.sluv.server.domain.user.dto.UserDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.exception.UserNotFoundException;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.jwt.JwtProvider;
import com.sluv.server.global.jwt.exception.InvalidateTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static com.sluv.server.domain.auth.enums.SnsType.GOOGLE;


@Service
@RequiredArgsConstructor
public class GoogleUserService {
    private final UserRepository userRepository;
    private final ClosetService closetService;
    private final JwtProvider jwtProvider;

    @Value("${spring.security.oauth2.client.android}")
    private String CLIENT_ANDROID;

    @Value("${spring.security.oauth2.client.apple}")
    private String CLIENT_APPLE;

    public AuthResponseDto googleLogin(AuthRequestDto request) {
        String idToken = request.getAccessToken();

        // 1. idToken 검증
        SocialUserInfoDto verifiedIdToken = verifyIdToken(idToken);

        // 2. user 정보로 DB 탐색 및 등록
        User googleUser = registerGoogleUserIfNeed(verifiedIdToken);

        // 3. userToken 생성
        return AuthResponseDto.builder()
                .token(createUserToken(googleUser))
                .build();
    }

    /**
     * == 프론트에서 준 idToken의 유효성 검사
     *
     * @param idToken
     * @return SocialUserInfoDto
     * @exception InvalidateTokenException
     */
    private SocialUserInfoDto verifyIdToken(String idToken){
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Arrays.asList(CLIENT_ANDROID, CLIENT_APPLE))
                .build();

        try {
            GoogleIdToken verifiedIdToken = verifier.verify(idToken);

            return convertResponseToSocialUserInfoDto(verifiedIdToken);
        }catch (Exception e){
            throw new InvalidateTokenException();
        }

    }

    /**
     * == GoogleIdToken을 SocialUserInfoDto로 변경
     *
     * @param idToken
     * @return SocialUserInfoDto
     */

    private static SocialUserInfoDto convertResponseToSocialUserInfoDto(GoogleIdToken idToken) {


        // responseBody에 있는 정보를 꺼냄
        String email = idToken.getPayload().getEmail();
        String profileImgUrl = (String) idToken.getPayload().get("picture");

        //Google에서 성별과 연령대 정보를 제공하지 않는 것 같음 23.3.5 -junker-

        return SocialUserInfoDto.builder()
                .email(email)
                .profileImgUrl(profileImgUrl)
                .gender(null)
                .ageRange(null)
                .build();
    }

    /**
     * == Google에서 받은 정보로 DB에서 유저 탐색 ==
     *
     * @param googleUserInfoDto
     * @return DB에 등록된 user
     * @throws , BaseException(NOT_FOUND_USER)
     */
    private User registerGoogleUserIfNeed(SocialUserInfoDto googleUserInfoDto) {
        User user = userRepository.findByEmail(googleUserInfoDto.getEmail()).orElse(null);

        if(user == null) {
            userRepository.save(User.builder()
                    .email(googleUserInfoDto.getEmail())
                    .snsType(GOOGLE)
                    .profileImgUrl(googleUserInfoDto.getProfileImgUrl())
                    .ageRange(googleUserInfoDto.getAgeRange())
                    .gender(googleUserInfoDto.getGender())
                    .build());

            user = userRepository.findByEmail(googleUserInfoDto.getEmail())
                                            .orElseThrow(UserNotFoundException::new);
        }

        // 생성과 동시에 기본 Closet 생성
        closetService.postBasicCloset(user);

        return user;
    }

    /**
     * == user 정보를 기반으로 user Access Token 생성 ==
     *
     * @param user
     * @return user Access Token
     */
    private String createUserToken(User user) {

        return jwtProvider.createAccessToken(UserDto.builder().id(user.getId()).build());
    }

}
