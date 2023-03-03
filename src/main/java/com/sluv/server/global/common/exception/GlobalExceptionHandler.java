package com.sluv.server.global.common.exception;

import com.sluv.server.global.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.sluv.server.global.common.exception.ErrorCode.*;
import static com.sluv.server.global.common.exception.ErrorCode.DB_ACCESS_ERROR;
import static com.sluv.server.global.common.exception.ErrorCode.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String LOG_FORMAT = "Class : {}, Message : {}";
    private static final String LOG_CODE_FORMAT = "Class : {}, Code : {}, Message : {}";

    /**
     * == Application Exception ==
     *
     * @exception ApplicationException
     * @return Each errorCode
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> applicationException(ApplicationException exception){
        log.error(
                LOG_CODE_FORMAT,
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

    /**
     * == vaildation Exception ==
     *
     * @exception MethodArgumentNotValidException
     * @return INVALID_ARGUMENT
     */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error(
                LOG_CODE_FORMAT,
                exception.getClass(),
                INVALID_ARGUMENT.getCode(),
                INVALID_ARGUMENT.getMessage()
        );

        return ResponseEntity.badRequest()
                .body(ErrorResponse.customBuilder()
                        .errorCode(INVALID_ARGUMENT)
                        .build()
                );
    }

    /**
     * == 런타임 Exception ==
     *
     * @exception RuntimeException
     * @return INTERNAL_SERVER_ERROR
     */

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> runtimeException(RuntimeException exception){
       log.error(
               LOG_FORMAT,
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
     * @exception DataAccessException
     * @return DB_ACCESS_ERROR
     */

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> dataAccessException(DataAccessException exception) {
        log.error(
                LOG_FORMAT,
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
     * @exception Exception
     * @return INTERNAL_SERVER_ERROR
     */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error(
                LOG_FORMAT,
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
