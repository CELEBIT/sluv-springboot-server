package com.sluv.server.global.common.exception;

import static com.sluv.server.global.common.exception.ErrorCode.DB_ACCESS_ERROR;
import static com.sluv.server.global.common.exception.ErrorCode.ENUM_ERROR;
import static com.sluv.server.global.common.exception.ErrorCode.INTERNAL_SERVER_ERROR;

import com.sluv.server.global.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String LOG_FORMAT = "Error: {}, Class : {}, Message : {}";
    private static final String LOG_CODE_FORMAT = "Error: {}, Class : {}, Code : {}, Message : {}";

    /**
     * == Application Exception ==
     *
     * @return Each errorCode
     * @throws ApplicationException
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> applicationException(ApplicationException exception) {
        log.error(
                LOG_CODE_FORMAT,
                "ApplicationException",
                exception.getClass().getSimpleName(),
                exception.getErrorCode(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(ErrorResponse.builder()
                        .code(exception.getErrorCode())
                        .message(exception.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(HttpMessageNotReadableException exception) {
        log.error(
                LOG_FORMAT,
                "HttpMessageNotReadableException",
                exception.getClass().getSimpleName(),
                exception.getMessage()
        );

        return ResponseEntity.badRequest()
                .body(ErrorResponse.customBuilder()
                        .errorCode(ENUM_ERROR)
                        .build()
                );
    }


    /**
     * == 런타임 Exception ==
     *
     * @return INTERNAL_SERVER_ERROR
     * @throws RuntimeException
     */

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> runtimeException(RuntimeException exception) {
        log.error(
                LOG_FORMAT,
                "RuntimeException",
                exception.getClass().getSimpleName(),
                exception.getMessage()
        );

        return ResponseEntity.internalServerError()
                .body(ErrorResponse.customBuilder()
                        .errorCode(INTERNAL_SERVER_ERROR)
                        .build()
                );
    }

    /**
     * == DB Exception ==
     *
     * @return DB_ACCESS_ERROR
     * @throws DataAccessException
     */

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> dataAccessException(DataAccessException exception) {
        log.error(
                LOG_FORMAT,
                "DataAccessException",
                exception.getClass().getSimpleName(),
                exception.getMessage()
        );

        return ResponseEntity.badRequest()
                .body(ErrorResponse.customBuilder()
                        .errorCode(DB_ACCESS_ERROR)
                        .build()
                );
    }

    /**
     * == 기타 Exception ==
     *
     * @return INTERNAL_SERVER_ERROR
     * @throws Exception
     */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error(
                LOG_FORMAT,
                "Exception",
                exception.getClass().getSimpleName(),
                exception.getMessage()
        );
        return ResponseEntity.internalServerError()
                .body(ErrorResponse.customBuilder()
                        .errorCode(INTERNAL_SERVER_ERROR)
                        .build()
                );

    }

}
