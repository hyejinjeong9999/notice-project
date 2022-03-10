package com.example.noticeproject.common.controller.response;

import lombok.Data;

@Data
public class ErrorResponse {

    private final Meta meta;

    public ErrorResponse(String errorCode, String errorMessage) {
        this.meta = new Meta.Fail(errorCode, errorMessage);
    }

    public ErrorResponse(ErrorCode e) {
        this(
            e.getKey(),
            e.getMessage()
        );
    }
}
