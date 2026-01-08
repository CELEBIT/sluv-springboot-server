package com.sluv.domain.config;

import com.sluv.domain.config.QueryDslConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.sluv.domain")
@EnableJpaRepositories(basePackages = "com.sluv.domain")
@EntityScan(basePackages = "com.sluv.domain")
@Import(QueryDslConfig.class)
public class TestConfig {
}
