package com.sluv.server.global.jwt.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sluv.server.global.common.BaseException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        ObjectMapper objectMapper = new ObjectMapper();
        BaseException baseException = null;

            log.error(authException.getMessage());
            final String expired = (String) request.getAttribute("expired");
            System.out.println("expired: " + expired);
            if (expired!=null){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "요휴시간 만료");
                log.error("유효시간 만료");
            }else{
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
                log.error("유효하지 않은 토큰");
            }
//
//        response.setStatus(401);
//        response.setContentType("application/json");
//        response.setCharacterEncoding("utf-8");
//        response.getWriter().write(objectMapper.writeValueAsString(baseException.fillInStackTrace()));
//        log.info(objectMapper.writeValueAsString(baseException.fillInStackTrace()));
    }
}
