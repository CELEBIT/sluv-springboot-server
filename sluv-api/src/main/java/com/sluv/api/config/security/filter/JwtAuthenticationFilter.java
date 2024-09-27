package com.sluv.api.config.security.filter;

import com.sluv.api.auth.dto.MemberDetails;
import com.sluv.common.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.sluv.common.constant.ConstantData.ACCESS_TOKEN_HEADER;
import static com.sluv.common.constant.ConstantData.TOKEN_TYPER;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = resolveToken(request);
        if (token != null && jwtProvider.validateToken(token)) {
            Authentication authentication = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        return request.getHeader(ACCESS_TOKEN_HEADER);
    }

    private Authentication getAuthentication(String headerToken) {
        String token = headerToken.split(TOKEN_TYPER + " ")[1];
        Long userId = jwtProvider.getUserId(token);
        UserDetails userDetails = new MemberDetails(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, headerToken, userDetails.getAuthorities());
    }

}
