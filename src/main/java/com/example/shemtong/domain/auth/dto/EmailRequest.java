package com.example.shemtong.domain.auth.dto;

import lombok.Getter;

@Getter
public class EmailRequest {

    private String mail;

    private String verifyCode;

}
