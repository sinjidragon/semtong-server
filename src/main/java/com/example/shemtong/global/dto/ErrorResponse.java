package com.example.shemtong.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@SuperBuilder
public class ErrorResponse {

    private final String errorCode;

    public final String message;

}
