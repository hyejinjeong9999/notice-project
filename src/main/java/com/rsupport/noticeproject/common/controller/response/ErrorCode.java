package com.rsupport.noticeproject.common.controller.response;

public enum ErrorCode {

    NOTICE_NOT_FOUND("게시글 정보가 존재하지 않습니다."),
    USER_NOT_FOUND("유저가 존재하지 않습니다."),
    INCORRECT_PERMISSION("유저 권한이 없습니다"),
    FILE_NOT_FOUND("파일정보가 없습니다");


    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getKey() {
        return name().replace("_", "-").toLowerCase();
    }

    public String getMessage() {
        return message;
    }
}
