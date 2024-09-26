package com.sluv.common.jwt;

import com.sluv.common.jwt.exception.ExpiredTokenException;
import com.sluv.common.jwt.exception.InvalidateTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey = "secretKey";

    @Value("${jwt.expiration-seconds}")
    private Long tokenValidMillisecond = 0L;

    @Value("${jwt.type}")
    private String tokenType;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private Key getSigninKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    /**
     * === user Access Token 생성 ===
     *
     * @return Access Token
     */
    public String createAccessToken(Long userId) {
        Claims claims = Jwts.claims().setSubject(userId.toString());
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
    public Long getUserId(String token) {
        String info = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes()).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return Long.valueOf(info);
    }

    /**
     * === token 만료 확인 ===
     *
     * @param _token
     * @return true or false
     * @throws ExpiredTokenException
     */
    public boolean validateToken(String _token) {
        try {
//            String token = _token.split(tokenType + " ")[1];
            String token = _token.split("Bearer" + " ")[1];
            Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build()
                    .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            // 잘못된 토큰
            throw new InvalidateTokenException();
        } catch (ExpiredJwtException e) {
            // 만료된 토큰
            throw new ExpiredTokenException();
        } catch (UnsupportedJwtException e) {
            // 지원하지 않는 토큰
        } catch (Exception e) {
            //나머지 예외
            throw new InvalidateTokenException();
        }

        return false;
    }

    public Boolean checkClaimsJwsBodyIsNull(PublicKey publicKey, String identityToken) {
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build();

        return parser.parseClaimsJws(identityToken).getBody() == null;
    }
}
