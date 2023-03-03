package com.sluv.server.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
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
                .description("Sluv Swagger" +
                        "\n- === Status Code ===" +
                        "\n- == 1000 == -> 성공" +
                        "\n- 1000: 요청성공" +
                        "\n- == 2000 == -> User 관련" +
                        "\n- 2000: 존재하지 않는 유저" +
                        "\n- == 3000 == -> Validation 관련" +
                        "\n- 3000: InValidate" +
                        "\n- == 4000 == -> Token 관련" +
                        "\n- 4000: 토큰 없음" +
                        "\n- 4001: 유효하지 않는 토큰" +
                        "\n- 4002: 만료된 토큰" +
                        "\n- == 5000 == -> 서버 내부 에러" +
                        "\n- 5000: 서버 내부 에러" +
                        "\n- 5001: DB 에러");


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
                .addSecurityItem(securityRequirement)
                .components(components);
    }

    @Bean
    public GroupedOpenApi allGroup(){
        return GroupedOpenApi.builder()
                .group("All")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public GroupedOpenApi authGroup(){
        return GroupedOpenApi.builder()
                .group("Auth")
                .pathsToMatch("/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userGroup(){
        return GroupedOpenApi.builder()
                .group("User")
                .pathsToMatch("/user/**")
                .build();
    }

}