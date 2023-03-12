package com.sluv.server.domain.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sluv.server.domain.auth.dto.AuthRequestDto;
import com.sluv.server.domain.auth.dto.AuthResponseDto;
import com.sluv.server.domain.auth.dto.SocialUserInfoDto;
import com.sluv.server.domain.user.dto.UserDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.domain.user.exception.NotFoundUserException;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.jwt.JwtProvider;
import com.sluv.server.global.jwt.exception.InvalidateTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.sluv.server.domain.auth.enums.SnsType.GOOGLE;


@Service
@RequiredArgsConstructor
public class GoogleUserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public AuthResponseDto googleLogin(AuthRequestDto request) throws JsonProcessingException {
        String accessToken = request.getAccessToken();

        // 1. accessToken으로 user 정보 요청
        SocialUserInfoDto googleUserInfo = getGoogleUserInfo(accessToken);

        // 2. user 정보로 DB 탐색 및 등록
        User googleUser = registerGoogleUserIfNeed(googleUserInfo);

        // 3. userToken 생성
        return AuthResponseDto.builder()
                .token(createUserToken(googleUser))
                .build();
    }

    /**
     * == Front에서 준 accessToken으로 Google에게 유저 정보 요청 ==
     *
     * @param accessToken
     * @return 유저 정보
     * @throws JsonProcessingException, , BaseException(JWT_AUTHENTICATION_FAILED)
     */
    private SocialUserInfoDto getGoogleUserInfo(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);

        RestTemplate rt = new RestTemplate();
        try{
            ResponseEntity<String> response = rt.exchange(
                    "https://www.googleapis.com/oauth2/v1/userinfo",
                    HttpMethod.GET,
                    kakaoUserInfoRequest,
                    String.class
            );

            return convertResponseToSocialUserInfoDto(response);
        }catch (Exception e){
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
    private static SocialUserInfoDto convertResponseToSocialUserInfoDto(ResponseEntity<String> response) throws JsonProcessingException {
        // responseBody에 있는 정보를 꺼냄
        String responseBody = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String email = jsonNode.get("email").asText();
        String profileImgUrl = jsonNode.get("picture").asText();

        //Google에서 성별과 연령대 정보를 제공하지 않는 것 같음 23.3.5 -junker-
//        String gender;
//
//        try{
//            gender = jsonNode.get("gender").asText();
//        }catch (Exception e){
//            gender = null;
//        }
//
//        String ageRange;
//        try{
//            ageRange = jsonNode.get("age_range").asText();
//        }catch (Exception e){
//            ageRange = null;
//        }

        return SocialUserInfoDto.builder()
                .email(email)
                .profileImgUrl(profileImgUrl)
                .gender(null)
                .ageRange(null)
                .build();
    }

    /**
     * == KAKAO에서 받은 정보로 DB에서 유저 탐색 ==
     *
     * @param googleUserIngoDto
     * @return DB에 등록된 user
     * @throws , BaseException(NOT_FOUND_USER)
     */
    private User registerGoogleUserIfNeed(SocialUserInfoDto googleUserIngoDto) {
        User user = userRepository.findByEmail(googleUserIngoDto.getEmail()).orElse(null);

        if(user == null) {
            userRepository.save(User.builder()
                    .email(googleUserIngoDto.getEmail())
                    .snsType(GOOGLE)
                    .profileImgUrl(googleUserIngoDto.getProfileImgUrl())
                    .ageRange(googleUserIngoDto.getAgeRange())
                    .gender(googleUserIngoDto.getGender())
                    .build());

            user = userRepository.findByEmail(googleUserIngoDto.getEmail())
                                            .orElseThrow(NotFoundUserException::new);
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
