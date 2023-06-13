package com.sluv.server.domain.search.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class ElasticSearchConnectUtil {
    @Value("${spring.elasticsearch.uri}")
    private String ELASTIC_SEARCH_URI;

    /**
     * ElasticSearch API 호출
     * @param keyword
     * @param path
     * @return result Id List
     */
    public List<Long> connectElasticSearch(String keyword, String path){
        //요청 URL 조립
        String requestUrl = ELASTIC_SEARCH_URI + path + "?searchTerm=" + keyword;

        // API 호출
        ResponseEntity<Long[]> elasticSearchResponse = getElasticSearchResponse(requestUrl);

        // 요청 결과 값 반환
        return elasticSearchResponse.getBody() != null
                ? Arrays.stream(elasticSearchResponse.getBody()).toList()
                : null;
    }

    /**
     * ReqstTemplate로 다른 서버의 API 호출
     * @param requestUrl
     * @return ResponseEntity<Long[]> 결과 아이디
     */
    private ResponseEntity<Long[]> getElasticSearchResponse(String requestUrl) {
        // Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        // Request
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        // RestTemplate
        log.info("Request: GET {}", requestUrl);
        RestTemplate rt = new RestTemplate();
        return rt.exchange(
                requestUrl,
                HttpMethod.GET,
                request,
                Long[].class
        );
    }
}
