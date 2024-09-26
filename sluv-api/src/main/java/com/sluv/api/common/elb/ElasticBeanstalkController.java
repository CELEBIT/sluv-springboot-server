package com.sluv.api.common.elb;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ElasticBeanstalkController {

    @GetMapping("/")
    public HttpStatusCode elasticBeanstalkOK() {
        return HttpStatusCode.valueOf(200);
    }
}
