package com.sluv.server.global.config.security;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable().cors().disable()// csrf, cors -> disable.
                .authorizeHttpRequests((request) -> request // 허용 범위 설정
                        .requestMatchers("/**").permitAll() // 허용범위
                        .anyRequest().authenticated() // 나머지는 authenticated되어야 함.
                )
                .formLogin((form) -> form // authenticated 되지 않은 곳에 접속 시 redirect
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }
}
