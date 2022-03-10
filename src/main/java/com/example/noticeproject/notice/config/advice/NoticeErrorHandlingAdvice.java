package com.example.noticeproject.notice.config.advice;

import com.example.noticeproject.common.controller.response.ErrorCode;
import com.example.noticeproject.common.controller.response.ErrorResponse;
import com.example.noticeproject.notice.service.exception.FileNotFoundException;
import com.example.noticeproject.notice.service.exception.NoticeNotfoundException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Getter
public class NoticeErrorHandlingAdvice {

    /**
     * 찾는 게시글이 없을때 Exception 처리
     *
     * @param e
     * @return
     */
    @ExceptionHandler(NoticeNotfoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse userNotFound(RuntimeException e) {
        log.error("Notice Not Found", e);
        return new ErrorResponse(ErrorCode.NOTICE_NOT_FOUND);
    }

    /**
     * 찾는 파일이 없을때 Exception 처리
     *
     * @param e
     * @return
     */
    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse fileNotFound(RuntimeException e) {
        log.error("File Not Found", e);
        return new ErrorResponse(ErrorCode.FILE_NOT_FOUND);
    }

}

