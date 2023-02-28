package com.sluv.server.global.jwt;


import com.sluv.server.domain.user.dto.UserDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import java.security.Key;

import java.util.Base64;
import java.util.Date;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {
    private final CustomUserDetailsService customUserDetailsService;

    @Value("${jwt.secret}")
    private String secretKey = "secretKey";

    @Value("${jwt.expiration-seconds}")
    private Long tokenValidMillisecond = 0L;

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }
    private Key getSigninKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Authentication getAuthentication(String token){
        UserDetails user = customUserDetailsService.loadUserByUsername(this.getUserId(token).toString());

        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }

    /**
     * === user Access Token 생성 ===
     *
     * @param user
     * @return Access Token
     */
    public String createAccessToken(UserDto user){
        Long id = user.getId();

        Claims claims = Jwts.claims().setSubject(id.toString());
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMillisecond))
                .signWith(getSigninKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * === token에서 user id 추출 ===
     *
     * @param token
     * @return user's id
     */
    public Long getUserId(String token){
        String info = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes()).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return Long.valueOf(info);
    }

    /**
     * === HttpServletRequest에서 token 추출 ===
     *
     * @param request
     * @return token
     */
    public String resolveToken(HttpServletRequest request){
        return request.getHeader("X-AUTH-TOKEN");
    }

    /**
     * === token 만료 확인 ===
     *
     * @param token
     * @return true or false
     */
    public boolean validateToken(HttpServletRequest request, String token){
        System.out.println("validate");
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token");
            request.setAttribute("expired",e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty");

        }
        return false;

    }
}
