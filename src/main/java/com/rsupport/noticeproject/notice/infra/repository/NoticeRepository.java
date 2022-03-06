package com.rsupport.noticeproject.notice.infra.repository;

import com.rsupport.noticeproject.notice.entity.Notice;
import com.rsupport.noticeproject.notice.infra.repository.custom.NoticeRepositoryCustom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryCustom {

    Optional<Notice> findByUser_UserIdAndNoticeNo(String userId, Long noticeNo);

    Optional<Notice> findByTitle(String title);

}
