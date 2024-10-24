package com.sluv.admin.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/*
 * Spring Security 설정 파일
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((request) -> request // 허용 범위 설정
                        .requestMatchers("/admin/login").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf().disable()
                .cors()
                .and()
                .formLogin(formLogin -> {
                    formLogin.loginPage("/admin/login")
                            .defaultSuccessUrl("/admin/home", true)
                            .failureUrl("/login?error=true")
                            .usernameParameter("email")
                            .passwordParameter("password")
                            .loginProcessingUrl("/admin/login-process");
                })
                .logout(logout -> {
                    logout.logoutUrl("/admin/logout-process");
                })
                .httpBasic().disable();
//                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화 방식
    }


}
