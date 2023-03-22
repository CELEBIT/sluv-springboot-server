package com.sluv.server.global.config.security;

import com.sluv.server.global.jwt.filter.ExceptionHandlerFilter;
import com.sluv.server.global.jwt.filter.JwtAuthenticationFilter;
import com.sluv.server.global.jwt.JwtProvider;
import com.sluv.server.global.jwt.exception.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final JwtProvider jwtProvider;
    private final ExceptionHandlerFilter exceptionHandlerFilter = new ExceptionHandlerFilter();
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private static final String[] PERMIT_URL = {
            // Elastic Beanstalk
            "/",

            // Swagger
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**",

            // Auth
            "/app/auth/test", "/app/auth/social-login",

            // Brand
            "/app/brand/search", "/app/brand/top",

            // Celeb
            "/app/celeb/search", "/app/celeb/top",

            // Item
            "/app/item/{itemId}", "/app/item/category", "/app/item/hashtag", "/app/item/place/top",

            // Notice
            "/app/notice", "/app/notice/{noticeId}",

            // Closet
            "/app/closet/{closetId}"

    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .cors().disable()
                // stateless
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 접근 설정
                .and()
                .formLogin().disable()
                .httpBasic().disable()

                .authorizeHttpRequests((request) -> request // 허용 범위 설정
//                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll() // 허용범위
                        .requestMatchers(PERMIT_URL).permitAll() // 허용범위
                        .anyRequest().authenticated()
                )
                .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class);





        return http.build();
    }


}
