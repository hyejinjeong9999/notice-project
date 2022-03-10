package com.example.noticeproject.common.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SingleResponse<T> {
    private final T content;
    private final Meta meta;

    public static class Ok<T> extends SingleResponse<T> {
        public Ok(T content){
            super(content, new Meta.Ok());
        }
    }
}
