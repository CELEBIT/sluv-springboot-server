package com.sluv.domain.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = {"com.sluv.domain"})
@EntityScan(basePackages = {"com.sluv.domain"})
@EnableJpaRepositories(basePackages = {"com.sluv.domain"})
public class JpaConfig {
}
