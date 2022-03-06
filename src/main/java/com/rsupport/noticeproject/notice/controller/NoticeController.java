package com.rsupport.noticeproject.notice.controller;

import static java.util.stream.Collectors.toList;

import com.rsupport.noticeproject.common.controller.response.EmptyResponse;
import com.rsupport.noticeproject.common.controller.response.ListResponse;
import com.rsupport.noticeproject.common.controller.response.Paging;
import com.rsupport.noticeproject.notice.controller.request.NoticeCreateRequest;
import com.rsupport.noticeproject.notice.controller.request.NoticeUpdateRequest;
import com.rsupport.noticeproject.notice.controller.request.PagingRequest;
import com.rsupport.noticeproject.notice.controller.response.FileDownloadResponse;
import com.rsupport.noticeproject.notice.controller.response.NoticeResponse;
import com.rsupport.noticeproject.notice.entity.Notice;
import com.rsupport.noticeproject.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "notices", description = "공지사항 관련 api 제공")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지사항 리스트", description = "공지사항 리스트 출력")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public ListResponse<NoticeResponse> getNoticeList(PagingRequest pagingRequest) {
        Page<Notice> noticeList = noticeService.findNoticeList(pagingRequest);

        return new ListResponse.Ok<>(
            noticeList.stream()
                .map(NoticeResponse::new)
                .collect(toList()),
            Paging.builder()
                .perPage(noticeList.getSize())
                .currentPage(noticeList.getNumber())
                .totalPage(noticeList.getTotalPages())
                .totalCount(noticeList.getTotalElements())
                .build()

        );
    }

    @Operation(summary = "첨부파일가져오기", description = "첨부파일 가져오기")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("attach/{fileNo}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long fileNo)
        throws MalformedURLException {
        FileDownloadResponse fileDownloadResponse = noticeService.downloadAttach(fileNo);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, fileDownloadResponse.getContentDisposition())
            .body(fileDownloadResponse.getResource());
    }

    @Operation(summary = "공지사항 상세내용", description = "공지사항 상세내용 출력")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{no}")
    public NoticeResponse getNoticeDetail(@NotNull @PathVariable Long no) {
        return noticeService.findNoticeDetail(no);
    }

    @Operation(summary = "공지사항 추가", description = "공지사항을 추가")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(consumes = {"multipart/form-data"})
    public NoticeResponse createNotice(NoticeCreateRequest request,
        @Nullable @RequestParam("attachments") List<MultipartFile> attachments) throws IOException {
        Notice notice = noticeService.createNotice(request, attachments);
        return new NoticeResponse(notice);
    }

    @Operation(summary = "공지사항 수정", description = "공지사항을 수정")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(consumes = {"multipart/form-data"})
    public NoticeResponse updateNotice(NoticeUpdateRequest request,
        @Nullable @RequestParam("attachments") List<MultipartFile> attachments) throws IOException {
        Notice notice = noticeService.updateNotice(request, attachments);
        return new NoticeResponse(notice);
    }

    @Operation(summary = "공지사항 삭제", description = "공지사항 삭제")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping()
    public EmptyResponse delNotice(@NotNull Long no) {
        noticeService.deleteNotice(no);
        return new EmptyResponse.Ok();
    }
}
