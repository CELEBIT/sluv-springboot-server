package com.sluv.server.domain.auth.service;

import static com.sluv.server.domain.auth.enums.SnsType.KAKAO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sluv.server.domain.auth.dto.AuthRequestDto;
import com.sluv.server.domain.auth.dto.SocialUserInfoDto;
import com.sluv.server.domain.closet.service.ClosetService;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.enums.UserAge;
import com.sluv.server.domain.user.enums.UserGender;
import com.sluv.server.domain.user.exception.UserNotFoundException;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.jwt.exception.InvalidateTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class KakaoUserService {
    private final UserRepository userRepository;
    private final ClosetService closetService;

    public User kakaoLogin(AuthRequestDto request) throws JsonProcessingException {
        String accessToken = request.getAccessToken();
        // 1. accessToken으로 user 정보 요청
        SocialUserInfoDto userInfo = getKakaoUserInfo(accessToken);

        // 2. user 정보로 DB 탐색 및 등록
        return registerKakaoUserIfNeed(userInfo);
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
        try {
            RestTemplate rt = new RestTemplate();
            ResponseEntity<String> response = rt.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.POST,
                    kakaoUserInfoRequest,
                    String.class
            );

            return convertResponseToSocialUserInfoDto(response);
        } catch (Exception e) {
            throw new InvalidateTokenException();
        }
    }

    /**
     * == KAKAO API가 준 Response로 SocialUserInfoDto 생성
     *
     * @param response
     * @return SocialUserInfoDto
     * @throws JsonProcessingException
     */
    private static SocialUserInfoDto convertResponseToSocialUserInfoDto(ResponseEntity<String> response)
            throws JsonProcessingException {
        // responseBody에 있는 정보를 꺼냄
        String responseBody = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String email = jsonNode.get("kakao_account").get("email").asText();
        String profileImgUrl = jsonNode.get("properties")
                .get("profile_image").asText();

        String gender;
        try {
            gender = jsonNode.get("kakao_account").get("gender").asText();
        } catch (Exception e) {
            gender = null;
        }

        String ageRange;
        try {
            ageRange = jsonNode.get("kakao_account").get("gender").asText();
        } catch (Exception e) {
            ageRange = null;
        }

        return SocialUserInfoDto.builder()
                .email(email)
                .profileImgUrl(profileImgUrl)
                .gender(convertGender(gender))
                .ageRange(convertAge(ageRange))
                .build();
    }

    /**
     * == KAKAO에서 받은 정보로 DB에서 유저 탐색 ==
     *
     * @param userInfoDto
     * @return DB에 등록된 user
     * @throws , BaseException(NOT_FOUND_USER)
     */
    private User registerKakaoUserIfNeed(SocialUserInfoDto userInfoDto) {
        User user = userRepository.findByEmail(userInfoDto.getEmail()).orElse(null);

        if (user == null) {
            userRepository.save(
                    User.toEntity(userInfoDto, KAKAO)
            );
            user = userRepository.findByEmail(userInfoDto.getEmail())
                    .orElseThrow(UserNotFoundException::new);

            // 생성과 동시에 기본 Closet 생성
            closetService.postBasicCloset(user);
        }

        return user;
    }

    private static UserGender convertGender(String gender) {
        UserGender userGender = UserGender.UNKNOWN;
        if (gender != null) {
            userGender = getUserGender(userGender, gender);
        }
        return userGender;
    }

    private static UserGender getUserGender(UserGender userGender, String gender) {
        if (gender.equals("male")) {
            userGender = UserGender.MALE;
        }
        if (gender.equals("female")) {
            userGender = UserGender.FEMALE;
        }
        return userGender;
    }

    private static UserAge convertAge(String age) {

        UserAge userGender = UserAge.UNKNOWN;
        if (age != null) {
            userGender = getUserAge(userGender, age);
        }
        return userGender;
    }

    private static UserAge getUserAge(UserAge userGender, String age) {
        int startAge = Integer.parseInt(age.split("~")[0]);
        for (UserAge value : UserAge.values()) {
            if (startAge == value.getStartAge()) {
                userGender = value;
                break;
            }
        }
        return userGender;
    }

}
