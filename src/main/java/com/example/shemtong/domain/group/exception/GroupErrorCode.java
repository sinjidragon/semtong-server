package com.example.shemtong.domain.group.exception;

import com.example.shemtong.global.exception.IErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum GroupErrorCode implements IErrorCode {
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "그룹을 찾을 수 없습니다."),
    USER_ALREADY_INGROUP(HttpStatus.BAD_REQUEST, "이미 그룹에 참가한 유저입니다."),
    USER_NOT_INGROUP(HttpStatus.BAD_REQUEST, "유저가 그룹에 속해있지 않습니다.")
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
