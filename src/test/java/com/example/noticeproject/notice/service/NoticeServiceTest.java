package com.example.noticeproject.notice.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.noticeproject.notice.controller.request.NoticeCreateRequest;
import com.example.noticeproject.notice.controller.request.NoticeUpdateRequest;
import com.example.noticeproject.notice.controller.request.PagingRequest;
import com.example.noticeproject.notice.entity.Notice;
import com.example.noticeproject.notice.infra.cache.NoticeCache;
import com.example.noticeproject.notice.infra.cache.NoticeCacheRepository;
import com.example.noticeproject.notice.infra.repository.NoticeRepository;
import com.example.noticeproject.user.entity.User;
import com.example.noticeproject.user.entity.value.Role;
import com.example.noticeproject.user.infra.repository.UserRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class NoticeServiceTest {

    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    NoticeCacheRepository noticeCacheRepository;
    @Autowired
    NoticeService noticeService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void init() {
        noticeRepository.deleteAll();
        noticeCacheRepository.deleteAll();
        addUser();
    }

    @Test
    @DisplayName("유효기간이 지난 데이터들을 제거하고 가져오는지 확인")
    void findNoticeList() {
        //given
        insertNotice();
        PagingRequest pagingRequest = new PagingRequest(0, 10);
        //when
        Page<Notice> noticeList = noticeService.findNoticeList(pagingRequest);
        //then
        assertThat(noticeList.getContent().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("redis에 저장된 데이터 확인, 조회수 +1 확인")
    void findNoticeDetail() {
        //given
        insertNotice();
        Notice notice = noticeRepository.findByTitle("testNotice").get();
        //when
        noticeService.findNoticeDetail(notice.getNoticeNo());
        NoticeCache noticeCache = noticeCacheRepository.findById(notice.getNoticeNo()).get();
        //then
        assertThat(noticeCache.getViews()).isEqualTo(1);
    }

    @Test
    @DisplayName("redis와 db에 데이터가 삭제되었는지 확인")
    void deleteNotice() {
        //given
        insertNotice();
        Notice notice = noticeRepository.findByTitle("testNotice").get();

        //when
        noticeService.deleteNotice(notice.getNoticeNo());

        //then
        assertThat(noticeRepository.findById(notice.getNoticeNo())).isEmpty();
        assertThat(noticeCacheRepository.findById(notice.getNoticeNo())).isEmpty();
    }

    @Test
    @DisplayName("데이터가 추가된 것을 확인")
    void createNotice() throws IOException {
        //given
        NoticeCreateRequest request = new NoticeCreateRequest();
        request.setUserId("testUser1");
        request.setTitle("testTitle");
        request.setContents("testContents");
        request.setNoticeStartDate(LocalDateTime.now());
        request.setNoticeEndDate(LocalDateTime.now().plusYears(10));

        //when
        noticeService.createNotice(request, null);

        //then
        assertThat(noticeRepository.findByTitle("testTitle")).isNotEmpty();
    }

    @Test
    @DisplayName("공지사항이 수정되고 레디스에 데이터가 없는 것을 확인")
    void updateNotice() throws IOException {
        //given
        insertNotice();
        Long no = noticeRepository.findByTitle("testNotice").get().getNoticeNo();

        NoticeUpdateRequest request = new NoticeUpdateRequest();
        request.setUserId("testUser1");
        request.setNoticeNo(no);
        request.setTitle("NEW testNotice");
        request.setContents("NEW testContents");
        request.setNoticeStartDate(LocalDateTime.now());
        request.setNoticeEndDate(LocalDateTime.now().plusYears(10));

        //when
        noticeService.updateNotice(request, null);

        //then
        assertThat(noticeRepository.findById(no).get().getTitle()).isEqualTo("NEW testNotice");
        assertThat(noticeCacheRepository.findById(no)).isEmpty();
    }

    @Test
    @DisplayName("redis에 있는 모든 데이터들이 db에 저장되는지 확인")
    void setNoticeViews() {
        //given
        insertNoticeDataAndCache();

        //when
        noticeService.setNoticeViews();

        //then
        assertThat(noticeRepository.findByTitle("testNotice").get().getViews()).isEqualTo(3);
        assertThat(noticeRepository.findByTitle("testNotice2").get().getViews()).isEqualTo(4);

    }

    void addUser() {
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
    }

    void insertNotice() {
        User user = userRepository.findByUserId("testUser1").get();
        Notice notice = Notice.builder()
            .user(user)
            .title("testNotice")
            .contents("testContents")
            .noticeStartDate(LocalDateTime.now())
            .noticeEndDate(LocalDateTime.now().plusMonths(1))
            .build();
        noticeRepository.save(notice);

        Notice notice2 = Notice.builder()
            .user(user)
            .title("testNotice2")
            .contents("testContents2")
            .noticeStartDate(LocalDateTime.now())
            .noticeEndDate(LocalDateTime.now().plusMonths(1))
            .build();
        noticeRepository.save(notice2);

        //expired time data
        Notice notice3 = Notice.builder()
            .user(user)
            .title("testNotice3")
            .contents("testContents3")
            .noticeStartDate(LocalDateTime.now().minusMonths(1))
            .noticeEndDate(LocalDateTime.now().minusMonths(1))
            .build();
        noticeRepository.save(notice3);
    }

    void insertNoticeDataAndCache() {
        User user = userRepository.findByUserId("testUser1").get();
        Notice notice = Notice.builder()
            .user(user)
            .title("testNotice")
            .contents("testContents")
            .noticeStartDate(LocalDateTime.now())
            .noticeEndDate(LocalDateTime.now().plusMonths(1))
            .build();
        noticeRepository.save(notice);

        Notice notice2 = Notice.builder()
            .user(user)
            .title("testNotice2")
            .contents("testContents2")
            .noticeStartDate(LocalDateTime.now())
            .noticeEndDate(LocalDateTime.now().plusMonths(1))
            .build();
        noticeRepository.save(notice2);

        Notice notice3 = Notice.builder()
            .user(user)
            .title("testNotice3")
            .contents("testContents3")
            .noticeStartDate(LocalDateTime.now())
            .noticeEndDate(LocalDateTime.now().plusMonths(1))
            .build();
        noticeRepository.save(notice3);

        NoticeCache noticeCache = NoticeCache.builder()
            .noticeNo(notice.getNoticeNo())
            .userName("testUser1")
            .title(notice.getTitle())
            .contents(notice.getContents())
            .createdAt(notice.getCreatedAt())
            .views(3)
            .build();
        noticeCacheRepository.save(noticeCache);

        NoticeCache noticeCache2 = NoticeCache.builder()
            .noticeNo(notice2.getNoticeNo())
            .userName("testUser1")
            .title(notice2.getTitle())
            .contents(notice2.getContents())
            .createdAt(notice2.getCreatedAt())
            .views(4)
            .build();

        noticeCacheRepository.save(noticeCache2);

    }


}