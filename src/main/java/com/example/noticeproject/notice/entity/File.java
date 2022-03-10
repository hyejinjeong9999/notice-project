package com.example.noticeproject.notice.entity;

import javax.persistence.Column;
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

    @Column(nullable = false, unique = true)
    private String storeFileName;

    @Column(nullable = false, unique = true)
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

