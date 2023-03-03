package com.sluv.server.domain.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sluv.server.domain.auth.dto.AuthRequestDto;
import com.sluv.server.domain.auth.dto.AuthResponseDto;
import com.sluv.server.domain.auth.dto.SocialUserInfoDto;
import com.sluv.server.domain.user.dto.UserDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.repository.UserRepository;

import com.sluv.server.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.sluv.server.domain.user.enums.SnsType.KAKAO;


@Service
@RequiredArgsConstructor
public class KakaoUserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public AuthResponseDto kakaoLogin(AuthRequestDto request) throws JsonProcessingException {
        String accessToken = request.getAccessToken();

        // 1. accessToken으로 user 정보 요청
        SocialUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 2. user 정보로 DB 탐색 및 등록
        User kakaoUser = registerKakaoUserIfNeed(kakaoUserInfo);

        // 3. userToken 생성
        return AuthResponseDto.builder()
                .token(createUserToken(kakaoUser))
                .build();
    }

    /**
     * == Front에서 준 accessToken으로 KAKAO에게 유저 정보 요청 ==
     *
     * @param accessToken
     * @return 유저 정보
     * @throws JsonProcessingException, , BaseException(JWT_AUTHENTICATION_FAILED)
     */
    private SocialUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);

        RestTemplate rt = new RestTemplate();
            ResponseEntity<String> response = rt.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.POST,
                    kakaoUserInfoRequest,
                    String.class
            );
        System.out.println("여기는?");
            return convertResponseToSocialUserInfoDto(response);
    }

    /**
     * == KAKAO API가 준 Response로 SocialUserInfoDto 생성
     *
     * @param response
     * @return SocialUserInfoDto
     * @throws JsonProcessingException
     */
    private static SocialUserInfoDto convertResponseToSocialUserInfoDto(ResponseEntity<String> response) throws JsonProcessingException {
        System.out.println("여기까진 오니?");
        // responseBody에 있는 정보를 꺼냄
        String responseBody = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String email = jsonNode.get("kakao_account").get("email").asText();
        String profileImgUrl = jsonNode.get("properties")
                .get("profile_image").asText();

        String gender;
        try{
            gender = jsonNode.get("kakao_account").get("gender").asText();
        }catch (Exception e){
            gender = null;
        }

        String ageRange;
        try{
            ageRange = jsonNode.get("kakao_account").get("age_range").asText();
        }catch (Exception e){
            ageRange = null;
        }

        return SocialUserInfoDto.builder()
                .email(email)
                .profileImgUrl(profileImgUrl)
                .gender(gender)
                .ageRange(ageRange)
                .build();
    }

    /**
     * == KAKAO에서 받은 정보로 DB에서 유저 탐색 ==
     *
     * @param kakaoUserInfo
     * @return DB에 등록된 user
     * @throws , BaseException(NOT_FOUND_USER)
     */
    private User registerKakaoUserIfNeed(SocialUserInfoDto kakaoUserInfo) {
        User user = userRepository.findByEmail(kakaoUserInfo.getEmail()).orElse(null);

        if(user == null) {
            userRepository.save(User.builder()
                    .email(kakaoUserInfo.getEmail())
                    .snsType(KAKAO)
                    .profileImgUrl(kakaoUserInfo.getProfileImgUrl())
                    .ageRange(kakaoUserInfo.getAgeRange())
                    .gender(kakaoUserInfo.getGender())
                    .build());

            user = userRepository.findByEmail(kakaoUserInfo.getEmail())
                                            .orElseThrow(() -> new IllegalArgumentException("유저 없음."));
        }
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
