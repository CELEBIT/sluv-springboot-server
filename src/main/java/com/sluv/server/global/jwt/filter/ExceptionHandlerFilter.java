package com.sluv.server.global.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sluv.server.global.common.response.ErrorResponse;
import com.sluv.server.global.jwt.exception.TokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (TokenException e) {
            setErrorResponse(response, e.getMessage(), e.getErrorCode(), e.getHttpStatus());
        }
//        } catch (MemberNotFoundException e) {
//            setErrorResponse(response, e.getMessage(), e.getErrorCode(),e.getHttpStatus());
//        }
    }

    private void setErrorResponse(HttpServletResponse response, String message, int errorCode, HttpStatus status)
            throws IOException {
        log.error("Code : {}, Message : {}", errorCode, message);
        String errorResponse = objectMapper.writeValueAsString(
                ErrorResponse.builder()
                        .code(errorCode)
                        .message(message)
                        .build()
        );
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(errorResponse);
    }
}
