package com.example.noticeproject.notice.infra.repository.custom;

import static com.example.noticeproject.notice.entity.QNotice.notice;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.example.noticeproject.notice.entity.Notice;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

public class NoticeRepositoryImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public NoticeRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Notice> findNoticeList(LocalDateTime now, Pageable pageable) {
        List<Notice> noticeList = queryFactory.selectFrom(notice)
            .where(notice.noticeStartDate.before(now)
                .and(notice.noticeEndDate.after(now)))
            .orderBy(notice.createdAt.desc())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(notice.count())
            .from(notice)
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset());

        return PageableExecutionUtils.getPage(noticeList, pageable, countQuery::fetchOne);
    }

}
