package com.example.noticeproject.common.controller.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ListResponse<T> {
    private final List<T> items;
    private final Meta meta;
    private final Paging paging;

    public static class Ok<T> extends ListResponse<T> {
        public Ok(List<T> items, Paging paging) {
            super(items, new Meta.Ok(), paging);
        }
    }
}
