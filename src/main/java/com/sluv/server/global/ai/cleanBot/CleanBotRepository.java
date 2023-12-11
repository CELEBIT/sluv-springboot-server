package com.sluv.server.global.ai.cleanBot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
@Slf4j
public class CleanBotRepository {
    @Value("${spring.ai-model.uri}")
    private String URL;

    public boolean isMaliciousComment(String comment) {
        boolean isMalicious = false;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(comment, headers);

        // RestTemplate
        log.info("Request: POST {}", URL);
        log.info("Comment {}", comment);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<Integer> response = rt.exchange(
                URL + "/cleanBot",
                HttpMethod.POST,
                request,
                Integer.class
        );

        if (response.getBody() == 2) {
            isMalicious = true;
        }

        return isMalicious;
    }
}
