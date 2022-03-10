package com.example.noticeproject.user.config.advice;

import com.example.noticeproject.common.controller.response.ErrorCode;
import com.example.noticeproject.common.controller.response.ErrorResponse;
import com.example.noticeproject.user.service.exception.IncorrectPermission;
import com.example.noticeproject.user.service.exception.UserNotfoundException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Getter
public class UserErrorHandlingAdvice {

    /**
     * 찾는 유저가 없을때 Exception 처리
     *
     * @param e
     * @return
     */
    @ExceptionHandler(UserNotfoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse userNotFound(RuntimeException e) {
        log.error("User Not Found", e);

        return new ErrorResponse(ErrorCode.USER_NOT_FOUND);
    }

    /**
     * 유저가 권한이 없을 때 Exception 처리
     *
     * @param e
     * @return
     */
    @ExceptionHandler(IncorrectPermission.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse incorrectPermission(RuntimeException e) {
        log.error("incorrect Permission", e);
        return new ErrorResponse(ErrorCode.INCORRECT_PERMISSION);
    }

}
