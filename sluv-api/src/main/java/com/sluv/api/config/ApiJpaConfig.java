package com.sluv.api.config;

import com.sluv.domain.config.JpaConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@Import(JpaConfig.class)
@EnableJpaAuditing
public class ApiJpaConfig {
}
