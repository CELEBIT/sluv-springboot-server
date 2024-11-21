package com.sluv.api.config.security;

import com.sluv.api.config.security.filter.CustomAuthenticationEntryPoint;
import com.sluv.api.config.security.filter.ExceptionHandlerFilter;
import com.sluv.api.config.security.filter.JwtAuthenticationFilter;
import com.sluv.common.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SpringSecurityConfig {

    @Value("${swagger.user}")
    private String SWAGGER_USER;

    @Value("${swagger.password}")
    private String SWAGGER_PASSWORD;

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
            "/app/item/{ItemId}/like",
            "/app/item/temp",
            "/app/item/temp/{tempItemId}",

            // user
            "/app/user/celeb",
            "/app/user/profile",

            // closet
            "/app/closet"

    };

    private static final String[] SWAGGER_URL = {"/swagger-ui/**", "/v3/api-docs/**"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
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
                        .requestMatchers(HttpMethod.GET, "/app/*/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/app/item/category").permitAll()
                        .requestMatchers(HttpMethod.GET, "/app/item/hashtag").permitAll()
                        .requestMatchers(AUTH_URL).authenticated() // 체크 범위.
                        .anyRequest().permitAll()
                )
                .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain swaggerFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(SWAGGER_URL)
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user =
                User.withUsername(SWAGGER_USER)
                        .password(passwordEncoder().encode(SWAGGER_PASSWORD))
                        .roles("SWAGGER")
                        .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }


}
