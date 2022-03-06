package com.rsupport.noticeproject.notice.infra.cache;

import com.rsupport.noticeproject.notice.controller.response.FileResponse;
import com.rsupport.noticeproject.notice.controller.response.NoticeResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "NoticeCache")
@Getter
@NoArgsConstructor
public class NoticeCache {

    @Id
    private Long noticeNo;
    private String userName;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
    private int views;
    private List<FileResponse> fileResponse;

    @Builder
    public NoticeCache(Long noticeNo, String userName, String title, String contents,
        LocalDateTime createdAt, int views, List<FileResponse> fileResponse) {
        this.noticeNo = noticeNo;
        this.userName = userName;
        this.title = title;
        this.contents = contents;
        this.createdAt = createdAt;
        this.views = views;
        this.fileResponse = fileResponse;
    }

    public NoticeCache getNoticeCache(NoticeResponse noticeResponse) {
        this.noticeNo = noticeResponse.getNoticeNo();
        this.userName = noticeResponse.getUserName();
        this.title = noticeResponse.getTitle();
        this.contents = noticeResponse.getContents();
        this.createdAt = noticeResponse.getCreatedAt();
        this.views = noticeResponse.getViews();
        this.fileResponse = noticeResponse.getFileResponse();
        return this;
    }

    public NoticeCache increaseViews() {
        this.views++;
        return this;
    }

}
