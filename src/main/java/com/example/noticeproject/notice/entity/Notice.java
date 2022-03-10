package com.example.noticeproject.notice.entity;

import com.example.noticeproject.common.entity.BaseTimeEntity;
import com.example.noticeproject.notice.controller.request.NoticeUpdateRequest;
import com.example.noticeproject.user.entity.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor
@DynamicUpdate
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long noticeNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false, unique = true)
    private String contents;

    @Column(nullable = false, unique = true)
    private LocalDateTime noticeStartDate;

    @Column(nullable = false, unique = true)
    private LocalDateTime noticeEndDate;

    private int views;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();


    @Builder
    public Notice(User user, String title, String contents, LocalDateTime noticeStartDate,
        LocalDateTime noticeEndDate, int views) {
        this.user = user;
        this.title = title;
        this.contents = contents;
        this.noticeStartDate = noticeStartDate;
        this.noticeEndDate = noticeEndDate;
        this.views = views;
    }

    public Notice updateNotice(NoticeUpdateRequest request, int views) {
        this.title = request.getTitle();
        this.contents = request.getContents();
        this.noticeStartDate = request.getNoticeStartDate();
        this.noticeEndDate = request.getNoticeEndDate();
        this.views = views;
        return this;
    }

    public Notice updateViews(int views) {
        this.views = views;
        return this;
    }

    public void updateFiles(List<File> files) {
        files.clear();
        files.addAll(files);
    }

    public void addFiles(List<File> files) {
        files.addAll(files);
    }
}
