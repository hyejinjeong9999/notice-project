package com.rsupport.noticeproject.notice.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue
    private Long fileNo;

    private String storeFileName;

    private String uploadFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Notice notice;

    @Builder
    public File(
        String storeFileName,
        String uploadFileName,
        Notice notice) {
        this.storeFileName = storeFileName;
        this.uploadFileName = uploadFileName;
        this.notice = notice;
    }
}

