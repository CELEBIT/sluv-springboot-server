package com.sluv.server.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class FcmConfig {

    @Value("${fcm.key.path}")
    private String FCM_PATH;

    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        ClassPathResource resource = new ClassPathResource(FCM_PATH);

        try (InputStream refreshToken = resource.getInputStream()) {
            FirebaseApp firebaseApp = findOrCreateFirebaseApp(refreshToken);
            return FirebaseMessaging.getInstance(firebaseApp);
        }
    }

    private FirebaseApp findOrCreateFirebaseApp(InputStream refreshToken) throws IOException {
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(refreshToken))
                .build();

        List<FirebaseApp> firebaseAppList = FirebaseApp.getApps();
        if (firebaseAppList != null) {
            return firebaseAppList.stream()
                    .filter(app -> app.getName().equals(FirebaseApp.DEFAULT_APP_NAME))
                    .findFirst()
                    .orElseGet(() -> FirebaseApp.initializeApp(options));
        } else {
            return FirebaseApp.initializeApp(options);
        }
    }

}
