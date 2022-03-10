package com.example.noticeproject.user.entity;

import com.example.noticeproject.common.entity.BaseTimeEntity;
import com.example.noticeproject.notice.entity.Notice;
import com.example.noticeproject.user.entity.value.Role;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long userNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String userPw;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notice> noticeList = new ArrayList<>();

    @Builder
    public User(Role role, String userId, String userName, String userPw,
        List<Notice> noticeList) {
        this.role = role;
        this.userId = userId;
        this.userName = userName;
        this.userPw = userPw;
        this.noticeList = noticeList;
    }
}
