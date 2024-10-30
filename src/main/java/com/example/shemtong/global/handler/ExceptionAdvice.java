package com.example.shemtong.global.handler;

import com.example.shemtong.global.dto.ErrorResponse;
import com.example.shemtong.global.exception.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        return ResponseEntity.status(e.code.getHttpStatus()).body(new ErrorResponse(e.code.getName(), e.code.getDescription()));
    }
}
