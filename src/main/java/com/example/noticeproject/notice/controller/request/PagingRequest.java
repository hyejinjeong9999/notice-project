package com.example.noticeproject.notice.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;
import javax.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class PagingRequest {

    @Positive
    @Schema(description = "볼 페이지", example = "0")
    private final Integer page;

    @Schema(description = "한 페이지당 불러올 개수", example = "10")
    @Positive
    private final Integer perPage;

    public PagingRequest(Integer page, Integer perPage) {
        this.page = Optional.ofNullable(page)
            .orElse(0);
        this.perPage = Optional.ofNullable(perPage)
            .orElse(10);
    }
}
