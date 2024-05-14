package com.sluv.server.global.discord;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DiscordWebHookConnector {
    public void sendMessageForDiscord(WebHookMessage message, String url) {

        // Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        // Request
        HttpEntity<WebHookMessage> request = new HttpEntity<>(message, headers);

        // RestTemplate
        RestTemplate rt = new RestTemplate();
        try {
            ResponseEntity<String> response = rt.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );
        } catch (Exception e) {
            throw new DiscordWebHookException();
        }

    }
}
