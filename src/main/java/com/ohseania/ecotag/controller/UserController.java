package com.ohseania.ecotag.controller;

import com.ohseania.ecotag.domain.userVO.request.SignUpForm;
import com.ohseania.ecotag.domain.userVO.response.UserInformation;
import com.ohseania.ecotag.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"User Information API"}, description = "유저 정보 관리 서비스")
@RestController
@AllArgsConstructor
@RequestMapping("/api/user-information")
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "회원가입 / 로그인 API")
    @PostMapping("/search-information")
    private ResponseEntity<UserInformation> updateUserInformation(@RequestBody SignUpForm signUpForm) {
        return userService.validateUserInformaion(signUpForm);  // 요청으로 온 회원 정보를 가지고
        // 회원 정보에 대한 검증을 하는 비즈니스 로직 호출
    }

}