package com.sluv.server.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    // 1개만 있는 경우
    @Bean
    public OpenAPI api() {
        Info info = new Info()
                .version("v1.0.0")
                .title("Sluv")
                .description("Sluv Swagger");

        // SecuritySecheme명
        String jwtSchemeName = "AccessToken";
        // API 요청헤더에 인증정보 포함
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        // SecuritySchemes 등록
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP) // HTTP 방식
                        .scheme("bearer")
                        .bearerFormat("JWT")); // 토큰 형식을 지정하는 임의의 문자(Optional

        return new OpenAPI()
                .info(info)
                .addServersItem(new Server().url("/"))
                .addSecurityItem(securityRequirement)
                .components(components);
    }

    @Bean
    public GroupedOpenApi allGroup() {
        return GroupedOpenApi.builder()
                .group("All")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public GroupedOpenApi appGroup() {
        return GroupedOpenApi.builder()
                .group("App")
                .pathsToMatch("/app/**")
                .build();
    }

    @Bean
    public GroupedOpenApi authGroup() {
        return GroupedOpenApi.builder()
                .group("Auth")
                .pathsToMatch("/app/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi brandGroup() {
        return GroupedOpenApi.builder()
                .group("Brand")
                .pathsToMatch("/app/brand/**")
                .build();
    }

    @Bean
    public GroupedOpenApi celebGroup() {
        return GroupedOpenApi.builder()
                .group("Celeb")
                .pathsToMatch("/app/celeb/**")
                .build();
    }

    @Bean
    public GroupedOpenApi closetGroup() {
        return GroupedOpenApi.builder()
                .group("Closet")
                .pathsToMatch("/app/closet/**")
                .build();
    }

    @Bean
    public GroupedOpenApi commentGroup() {
        return GroupedOpenApi.builder()
                .group("Comment")
                .pathsToMatch("/app/comment/**")
                .build();
    }

    @Bean
    public GroupedOpenApi itemGroup() {
        return GroupedOpenApi.builder()
                .group("Item")
                .pathsToMatch("/app/item/**")
                .build();
    }

    @Bean
    public GroupedOpenApi questionGroup() {
        return GroupedOpenApi.builder()
                .group("Question")
                .pathsToMatch("/app/question/**")
                .build();
    }

    @Bean
    public GroupedOpenApi searchGroup() {
        return GroupedOpenApi.builder()
                .group("Search")
                .pathsToMatch("/app/search/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userGroup() {
        return GroupedOpenApi.builder()
                .group("User")
                .pathsToMatch("/app/user/**")
                .build();
    }

    @Bean
    public GroupedOpenApi s3Group() {
        return GroupedOpenApi.builder()
                .group("S3")
                .pathsToMatch("/app/s3/**")
                .build();
    }

}