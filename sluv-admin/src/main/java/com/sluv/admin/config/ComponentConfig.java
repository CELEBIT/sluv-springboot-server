package com.sluv.admin.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.sluv.common", "com.sluv.infra"})
public class ComponentConfig {
}
