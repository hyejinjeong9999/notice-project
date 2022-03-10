package com.example.noticeproject.notice.infra.repository;

import com.example.noticeproject.notice.entity.File;
import com.example.noticeproject.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    void deleteAllByNotice(Notice notice);
}
