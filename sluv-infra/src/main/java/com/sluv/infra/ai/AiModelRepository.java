package com.sluv.infra.ai;

import com.sluv.infra.ai.dto.CommentCleanBotReqDto;
import com.sluv.infra.ai.dto.ItemColorCheckReqDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
@Slf4j
public class AiModelRepository {
    @Value("${ai-model.uri}")
    private String URL;

    public boolean isMaliciousComment(String comment) {
        try {
            boolean isMalicious = false;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CommentCleanBotReqDto> request = new HttpEntity<>(CommentCleanBotReqDto.of(comment), headers);

            // RestTemplate
            log.info("Request: POST {}", URL);
            log.info("Comment {}", comment);
            RestTemplate rt = new RestTemplate();
            ResponseEntity<Integer> response = rt.exchange(
                    URL + "/check-malicious-comment",
                    HttpMethod.POST,
                    request,
                    Integer.class
            );

            if (response.getBody() == 2) {
                isMalicious = true;
            }

            return isMalicious;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public String getItemColor(String imgUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<ItemColorCheckReqDto> request = new HttpEntity<>(ItemColorCheckReqDto.of(imgUrl), headers);

        // RestTemplate
        log.info("Request: POST {}", URL);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                URL + "/check-item-color",
                HttpMethod.POST,
                request,
                String.class
        );

        return response.getBody();
    }
}
