package com.sluv.api.common.exception;

import com.sluv.api.common.response.ErrorResponse;
import com.sluv.common.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.sluv.common.exception.ErrorCode.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String LOG_FORMAT = "Error: {}, Class : {}, Message : {}, Stack : {}";
    private static final String LOG_CODE_FORMAT = "Error: {}, Class : {}, Code : {}, Message : {}, Stack : {}";

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
                exception.getMessage(),
                exception.getStackTrace()
        );
        HttpStatus httpStatus = HttpStatus.valueOf(exception.getHttpStatusCode().getCode());

        return ResponseEntity
                .status(httpStatus)
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
                exception.getMessage(),
                exception.getStackTrace()
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
                exception.getMessage(),
                exception.getStackTrace()
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
                exception.getMessage(),
                exception.getStackTrace()
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
                exception.getMessage(),
                exception.getStackTrace()
        );
        return ResponseEntity.internalServerError()
                .body(ErrorResponse.customBuilder()
                        .errorCode(INTERNAL_SERVER_ERROR)
                        .build()
                );

    }

}
