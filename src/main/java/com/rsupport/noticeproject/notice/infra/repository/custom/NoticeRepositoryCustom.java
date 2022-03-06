package com.rsupport.noticeproject.notice.infra.repository.custom;

import com.rsupport.noticeproject.notice.entity.Notice;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {

    Page<Notice> findNoticeList(LocalDateTime now, Pageable pageable);
}
