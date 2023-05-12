package com.sluv.server.global.config.security;

import com.sluv.server.global.jwt.filter.ExceptionHandlerFilter;
import com.sluv.server.global.jwt.filter.JwtAuthenticationFilter;
import com.sluv.server.global.jwt.JwtProvider;
import com.sluv.server.global.jwt.exception.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

    private static final String[] AUTH_URL = {
            // auth
            "/app/auth/auto-login",

            // brand
            "/app/brand/recent",
            "/app/brand/recent/{brandId}",

            // celeb
            "/app/celeb/recent",
            "/app/celeb/recent/{celebId}",

            // item
            "/app/item",
            "/app/item/{ItemId}",
            "/app/item/{ItemId}/like",
            "/app/item/temp",
            "/app/item/temp/{tempItemId}",

            // user
            "/app/user/celeb"

    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .cors()
                .and()
                // stateless
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 접근 설정
                .and()
                .formLogin().disable()
                .httpBasic().disable()

                .authorizeHttpRequests((request) -> request // 허용 범위 설정
                                .requestMatchers(HttpMethod.OPTIONS, "/**/*").permitAll()
                                .requestMatchers(HttpMethod.GET, "/app/item/category").permitAll()
                                .requestMatchers(AUTH_URL).authenticated() // 허용범위
                        .anyRequest().permitAll()
                )
                .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class);





        return http.build();
    }


}
