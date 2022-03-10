package com.example.noticeproject.user.controller;

import com.example.noticeproject.common.controller.response.EmptyResponse;
import com.example.noticeproject.common.controller.response.EmptyResponse.Ok;
import com.example.noticeproject.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "users", description = "유저 회원가입 제공")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입(테스트용)", description = "테스트를 위한 유저 회원가입, 유저 5명을 생성한다")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("create")
    public EmptyResponse createUser() {
        userService.createUsesrs();
        return new Ok();
    }

}
