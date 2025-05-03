package org.contourgara.presentation;

import lombok.extern.slf4j.Slf4j;
import org.contourgara.infrastructure.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ErrorResponse handleCustomException(CustomException e) {
        log.error(e.getMessage(), e);
        return ErrorResponse.create(e, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return ErrorResponse.create(e, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
