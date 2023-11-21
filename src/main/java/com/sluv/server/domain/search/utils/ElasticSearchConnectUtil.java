package com.sluv.server.domain.search.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class ElasticSearchConnectUtil {
    @Value("${spring.elasticsearch.uri}")
    private String ELASTIC_SEARCH_URI;

    /**
     * ElasticSearch API 호출
     *
     * @param keyword
     * @param path
     * @return result Id List
     */
    public List<Long> connectElasticSearch(String keyword, String path) {
        //요청 URL 조립
        String requestUrl = ELASTIC_SEARCH_URI + path + "?searchTerm=" + keyword;

        // API 호출
        return getElasticSearchResponse(requestUrl);
    }

    /**
     * ReqstTemplate로 다른 서버의 API 호출
     *
     * @param requestUrl
     * @return ResponseEntity<Long [ ]> 결과 아이디
     */
    private List<Long> getElasticSearchResponse(String requestUrl) {
        // Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        // Request
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        // RestTemplate
        log.info("Request: GET {}", requestUrl);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<JSONArray> response = rt.exchange(
                requestUrl,
                HttpMethod.GET,
                request,
                JSONArray.class
        );

        return jsonArrayToList(Objects.requireNonNull(response.getBody()));


    }

    private List<Long> jsonArrayToList(JSONArray jsonArray) {
        List<Long> list = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Long[] longArray = objectMapper.readValue(jsonArray.toString(), Long[].class);
            list = Arrays.stream(longArray).toList();

        } catch (Exception e) {
            log.error("JSONArray To Long List Convert Error");
        }
        return list;

    }
}
