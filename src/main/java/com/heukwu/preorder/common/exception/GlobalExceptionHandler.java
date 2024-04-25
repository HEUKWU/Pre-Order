package com.heukwu.preorder.common.exception;

import com.heukwu.preorder.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ApiResponse<String> notFoundException(NotFoundException e) {

        return ApiResponse.fail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ApiResponse<String> businessException(BusinessException e) {

        return ApiResponse.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        log.info(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errors);
    }

    @ExceptionHandler(Throwable.class)
    public ApiResponse<String> methodThrowable(Throwable e) {
        log.error("예상치 못한 에러 발생", e);

        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 에러 발생. 문의가 필요하면 xxx 으로 연락주세요.");
    }
}
