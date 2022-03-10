package com.example.noticeproject.notice.infra.cache;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeCacheRepository extends CrudRepository<NoticeCache, Long> {

}
