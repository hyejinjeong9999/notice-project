package com.rsupport.noticeproject.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -251239420L;

    public static final QUser user = new QUser("user");

    public final com.rsupport.noticeproject.common.entity.QBaseTimeEntity _super = new com.rsupport.noticeproject.common.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ListPath<com.rsupport.noticeproject.notice.entity.Notice, com.rsupport.noticeproject.notice.entity.QNotice> noticeList = this.<com.rsupport.noticeproject.notice.entity.Notice, com.rsupport.noticeproject.notice.entity.QNotice>createList("noticeList", com.rsupport.noticeproject.notice.entity.Notice.class, com.rsupport.noticeproject.notice.entity.QNotice.class, PathInits.DIRECT2);

    public final EnumPath<com.rsupport.noticeproject.user.entity.value.Role> role = createEnum("role", com.rsupport.noticeproject.user.entity.value.Role.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath userId = createString("userId");

    public final StringPath userName = createString("userName");

    public final NumberPath<Long> userNo = createNumber("userNo", Long.class);

    public final StringPath userPw = createString("userPw");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

