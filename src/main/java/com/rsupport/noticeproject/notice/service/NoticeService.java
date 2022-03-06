package com.rsupport.noticeproject.notice.service;

import com.rsupport.noticeproject.notice.config.FileStore;
import com.rsupport.noticeproject.notice.config.UploadFile;
import com.rsupport.noticeproject.notice.controller.request.NoticeCreateRequest;
import com.rsupport.noticeproject.notice.controller.request.NoticeUpdateRequest;
import com.rsupport.noticeproject.notice.controller.request.PagingRequest;
import com.rsupport.noticeproject.notice.controller.response.FileDownloadResponse;
import com.rsupport.noticeproject.notice.controller.response.NoticeResponse;
import com.rsupport.noticeproject.notice.entity.File;
import com.rsupport.noticeproject.notice.entity.Notice;
import com.rsupport.noticeproject.notice.infra.cache.NoticeCache;
import com.rsupport.noticeproject.notice.infra.cache.NoticeCacheRepository;
import com.rsupport.noticeproject.notice.infra.repository.FileRepository;
import com.rsupport.noticeproject.notice.infra.repository.NoticeRepository;
import com.rsupport.noticeproject.notice.service.exception.FileNotFoundException;
import com.rsupport.noticeproject.notice.service.exception.NoticeNotfoundException;
import com.rsupport.noticeproject.user.entity.User;
import com.rsupport.noticeproject.user.entity.value.Role;
import com.rsupport.noticeproject.user.infra.repository.UserRepository;
import com.rsupport.noticeproject.user.service.exception.IncorrectPermission;
import com.rsupport.noticeproject.user.service.exception.UserNotfoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeCacheRepository noticeCacheRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final FileStore fileStore;

    /**
     * 공지사항 리스트를 받는다
     *
     * @param pagingRequest
     * @return
     */
    public Page<Notice> findNoticeList(PagingRequest pagingRequest) {
        Page<Notice> noticeList = noticeRepository.findNoticeList(LocalDateTime.now(),
            PageRequest.of(
                pagingRequest.getPage(),
                pagingRequest.getPerPage()
            ));
        return noticeList;
    }

    /**
     * 캐시저장과 함께 공지사항 상세내용을 받는다
     *
     * @param no
     * @return
     */
    public NoticeResponse findNoticeDetail(Long no) {
        NoticeResponse noticeResponse = noticeCacheRepository.findById(no)
            .map(NoticeResponse::new)
            .orElseGet(() ->
                noticeRepository.findById(no)
                    .map(NoticeResponse::new)
                    .orElseThrow(NoticeNotfoundException::new));

        return saveCache(noticeResponse);
    }

    public NoticeResponse saveCache(NoticeResponse notice) {
        NoticeCache noticeCache = new NoticeCache().getNoticeCache(notice);
        return new NoticeResponse(noticeCacheRepository.save(noticeCache.increaseViews()));
    }

    /**
     * 캐시와 함께 공지를 삭제한다
     *
     * @param no
     */
    @Transactional
    public void deleteNotice(Long no) {
        Notice notice = noticeRepository.findById(no).orElseThrow(NoticeNotfoundException::new);
        noticeCacheRepository.findById(no).ifPresent(n -> {
            noticeCacheRepository.delete(n);
        });
        noticeRepository.delete(notice);
    }

    /**
     * 공지를 생성한다
     *
     * @param request
     * @param attachments
     * @return
     * @throws IOException
     */
    @Transactional
    public Notice createNotice(NoticeCreateRequest request, List<MultipartFile> attachments)
        throws IOException {
        User user = userRepository.findByUserId(request.getUserId()).orElseThrow(
            UserNotfoundException::new);

        if (user.getRole().equals(Role.GENERAL)) {
            throw new IncorrectPermission();
        }

        Notice notice = noticeRepository.save(
            Notice.builder()
                .user(user)
                .title(request.getTitle())
                .contents(request.getContents())
                .noticeStartDate(request.getNoticeStartDate())
                .noticeEndDate(request.getNoticeEndDate())
                .build()
        );

        if (attachments != null) {
            List<UploadFile> attachFiles = fileStore.storeFiles(attachments);
            notice.addFiles(attachFiles.stream().map(file -> fileRepository.save(File.builder()
                    .uploadFileName(file.getUploadFileName())
                    .storeFileName(file.getStoreFileName())
                    .notice(notice)
                    .build()))
                .collect(Collectors.toList()));
        }

        return notice;
    }

    /**
     * 파일을 다운로드 받는다
     *
     * @param fileNo
     * @return
     */
    public FileDownloadResponse downloadAttach(Long fileNo) throws MalformedURLException {
        File file = fileRepository.findById(fileNo).orElseThrow(FileNotFoundException::new);
        UrlResource resource = new UrlResource(
            "file:" + fileStore.getFullPath(file.getStoreFileName()));
        String encodedUploadFileName = UriUtils.encode(file.getUploadFileName(),
            StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return new FileDownloadResponse(resource, contentDisposition);
    }

    /**
     * 캐시삭제와 함께 공지사항을 업데이트 한다
     *
     * @param request
     * @param attachments
     * @return
     * @throws IOException
     */
    @Transactional
    public Notice updateNotice(NoticeUpdateRequest request, List<MultipartFile> attachments)
        throws IOException {

        Optional<NoticeCache> resultCache = noticeCacheRepository.findById(request.getNoticeNo());
        int views = 0;

        if (resultCache.isPresent()) {
            NoticeCache noticeCache = resultCache.get();
            views = noticeCache.getViews();
            noticeCacheRepository.delete(noticeCache);
        }

        Notice notice = noticeRepository.findByUser_UserIdAndNoticeNo(request.getUserId(),
            request.getNoticeNo()).orElseThrow(NoticeNotfoundException::new);

        if (attachments != null) {
            fileRepository.deleteAllByNotice(notice);
            List<UploadFile> attachFiles = fileStore.storeFiles(attachments);
            List<File> files = attachFiles.stream().map(file -> fileRepository.save(File.builder()
                    .uploadFileName(file.getUploadFileName())
                    .storeFileName(file.getStoreFileName())
                    .notice(notice)
                    .build()))
                .collect(Collectors.toList());
            notice.updateFiles(files);
        }

        return notice.updateNotice(request, views);

    }

    /**
     * 한시간마다 캐시에 저장되어있던 조회수를 업데이트 한다
     */
    @Scheduled(cron = "* * 1 * * *")
    @Transactional
    public void setNoticeViews() {
        log.info("=========start update views========");
        Iterable<NoticeCache> allCache = noticeCacheRepository.findAll();
        for (NoticeCache cache : allCache) {
            Long no = cache.getNoticeNo();
            int view = cache.getViews();
            noticeRepository.findById(no).ifPresent(notice -> {
                notice.updateViews(view);
            });
        }
        log.info("=========end update views========");


    }


}