package com.example.shemtong.global.exception;

public class CustomException extends RuntimeException {
    public final IErrorCode code;

    public CustomException(IErrorCode code) {
        super(code.getDescription());
        this.code = code;
    }
}
