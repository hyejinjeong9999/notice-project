package com.rsupport.noticeproject.notice.controller.response;

import com.rsupport.noticeproject.notice.entity.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {

    private Long fileNo;
    private String fileName;

    public FileResponse(File file) {
        this.fileNo = file.getFileNo();
        this.fileName = file.getUploadFileName();
    }

}
