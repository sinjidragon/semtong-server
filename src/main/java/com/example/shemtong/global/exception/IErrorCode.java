package com.example.shemtong.global.exception;

import org.springframework.http.HttpStatus;

public interface IErrorCode {
    HttpStatus getHttpStatus();
    String getDescription();
    String getName();
}
