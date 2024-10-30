package com.example.shemtong.global.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum GlobalErrorCode implements IErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러"),
    ;

    public final HttpStatus status;
    public final String description;

    @Override
    public HttpStatus getHttpStatus() {
        return status;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getName() {
        return name();
    }
}
