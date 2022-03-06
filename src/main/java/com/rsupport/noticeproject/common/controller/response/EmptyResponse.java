package com.rsupport.noticeproject.common.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmptyResponse {
    private final Meta meta;

    public static class Ok extends EmptyResponse{
        public Ok(){
            super(new Meta.Ok());
        }
    }
}
