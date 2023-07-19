package com.sluv.server.global.firebase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.sluv.server.global.firebase.exception.FCMAccessTokenInvalidateException;
import com.sluv.server.global.firebase.exception.FCMConnectException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FCMService {

    @Value("${fcm.key.path}")
    private String FCM_PATH;

    @Value("${fcm.key.api}")
    private String FCM_API;

    @Value("${fcm.key.scope}")
    private String FCM_SCOPE;

    private final ObjectMapper objectMapper;

    /**
     * Firebase로 메세지 전송
     */

    public void sendMessageTo(String targetToken, String title, String body, String imgUrl) {
        try {
            // message JSON
            String message = makeMessage(targetToken, title, body, imgUrl);

            // API 호출
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(FCM_API)
                    .post(requestBody)
                    .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                    .build();

            Response response = client.newCall(request)
                    .execute();

            log.info("FCM Message Post : {}", response.body().string());

        }catch (Exception e){
            throw new FCMConnectException();
        }
    }

    /**
     * FCM 메세지 제작
     */
    private String makeMessage(String targetToken, String title, String body, String imgUrl){
        try {
            // Dto 제작
            FCMMessage fcmMessage = FCMMessage.builder()
                    .message(FCMMessage.Message.builder()
                            .token(targetToken)
                            .notification(FCMMessage.Notification.builder()
                                    .title(title)
                                    .body(body)
                                    .imgUrl(imgUrl)
                                    .build()
                            )
                            .build()
                    )
                    .validate_only(false)
                    .build();

            return objectMapper.writeValueAsString(fcmMessage);
        }catch (Exception e){
            throw new IllegalArgumentException();
        }
    }

    /**
     * FCM AccessToken 발급.
     */
    private String getAccessToken() {
        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(FCM_PATH).getInputStream())
                    .createScoped(List.of(FCM_SCOPE));
            googleCredentials.refreshIfExpired();

            return googleCredentials.getAccessToken().getTokenValue();
        }catch (Exception e){
            throw new FCMAccessTokenInvalidateException();
        }
    }


}
