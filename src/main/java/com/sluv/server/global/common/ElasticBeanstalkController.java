package com.sluv.server.global.common;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ElasticBeanstalkController {

    @GetMapping("/")
    public HttpStatusCode elasticBeanstalkOK(){
        return HttpStatusCode.valueOf(200);
    }
}
