package com.sluv.server.global.jwt;


import com.sluv.server.domain.user.dto.UserIdDto;
import com.sluv.server.domain.user.exception.UserNotFoundException;
import com.sluv.server.domain.user.repository.UserRepository;
import com.sluv.server.global.jwt.exception.ExpiredTokenException;
import com.sluv.server.global.jwt.exception.InvalidateTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {
    private final UserRepository userRepository;

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

    public Authentication getAuthentication(String _token) {
        String token = _token.split(tokenType + " ")[1];
        UserDetails user = userRepository.findById(Long.valueOf(this.getUserId(token).toString()))
                .orElseThrow(UserNotFoundException::new);

        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }

    /**
     * === user Access Token 생성 ===
     *
     * @param user
     * @return Access Token
     */
    public String createAccessToken(UserIdDto user) {
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
    public Long getUserId(String token) {
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
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
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
            String token = _token.split(tokenType + " ")[1];
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
}
