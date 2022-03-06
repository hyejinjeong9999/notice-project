package com.rsupport.noticeproject.notice.controller.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.core.io.UrlResource;

@Getter
@NoArgsConstructor
public class FileDownloadResponse {

    private UrlResource resource;
    private String contentDisposition;

    public FileDownloadResponse(UrlResource resource, String contentDisposition) {
        this.resource = resource;
        this.contentDisposition = contentDisposition;
    }
}
