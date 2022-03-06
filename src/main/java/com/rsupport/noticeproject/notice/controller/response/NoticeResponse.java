package com.rsupport.noticeproject.notice.controller.response;

import com.rsupport.noticeproject.notice.entity.Notice;
import com.rsupport.noticeproject.notice.infra.cache.NoticeCache;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Schema(description = "공지사항 response")
@AllArgsConstructor
public class NoticeResponse {

    @Schema(description = "공지사항 번호")
    private Long noticeNo;

    @Schema(description = "공지사항 제목")
    private String title;

    @Schema(description = "공지사항 내용")
    private String contents;

    @Schema(description = "작성일")
    private LocalDateTime createdAt;

    @Schema(description = "조회수")
    private int views;

    @Schema(description = "유저이름")
    private String userName;

    @Schema(description = "파일정보")
    private List<FileResponse> fileResponse;

    public NoticeResponse(Notice notice) {
        this(
            notice.getNoticeNo(),
            notice.getTitle(),
            notice.getContents(),
            notice.getCreatedAt(),
            notice.getViews(),
            notice.getUser().getUserName(),
            notice.getFiles().stream()
                .map(a -> new FileResponse(a.getFileNo(), a.getUploadFileName())).collect(
                    Collectors.toList())
        );

    }

    public NoticeResponse(NoticeCache notice) {
        this(
            notice.getNoticeNo(),
            notice.getTitle(),
            notice.getContents(),
            notice.getCreatedAt(),
            notice.getViews(),
            notice.getUserName(),
            notice.getFileResponse()
        );
    }


}
