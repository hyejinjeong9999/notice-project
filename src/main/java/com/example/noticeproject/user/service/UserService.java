package com.example.noticeproject.user.service;

import com.example.noticeproject.user.entity.User;
import com.example.noticeproject.user.entity.value.Role;
import com.example.noticeproject.user.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 테스트용 유저를 넣는다
     */
    public void createUsesrs() {
        User user1 = User.builder()
            .role(Role.ADMIN)
            .userId("testUser1")
            .userName("관리자")
            .userPw("testUser1")
            .build();

        userRepository.save(user1);

        User user2 = User.builder()
            .role(Role.GENERAL)
            .userId("testUser2")
            .userName("김유저")
            .userPw("testUser2")
            .build();

        userRepository.save(user2);

        User user3 = User.builder()
            .role(Role.GENERAL)
            .userId("testUser3")
            .userName("박유저")
            .userPw("testUser3")
            .build();

        userRepository.save(user3);

        User user4 = User.builder()
            .role(Role.GENERAL)
            .userId("testUser4")
            .userName("최유저")
            .userPw("testUser4")
            .build();

        userRepository.save(user4);

        User user5 = User.builder()
            .role(Role.GENERAL)
            .userId("testUser5")
            .userName("이유저")
            .userPw("testUser5")
            .build();

        userRepository.save(user5);

    }

}
