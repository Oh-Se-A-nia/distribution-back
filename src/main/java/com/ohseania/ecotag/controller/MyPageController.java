package com.ohseania.ecotag.controller;

import com.ohseania.ecotag.domain.complaintVO.response.MyComplaint;
import com.ohseania.ecotag.domain.contributionVO.response.MyContribution;
import com.ohseania.ecotag.entity.Complaint;
import com.ohseania.ecotag.service.myPage.MyPageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = {"MyPage API"}, description = "마이 페이지 서비스")
@RestController
@AllArgsConstructor
@RequestMapping("/api/my-page")
public class MyPageController {

    private final MyPageService myPageService;

    @ApiOperation(value = "나의 민원 정보 제공 API")
    @GetMapping("/my-complaint")
    private ResponseEntity<List<MyComplaint>> serveContributionInfo(
            @RequestParam(value = "userId") Long id
    ) {
        return myPageService.returnMyComplaintList(id);
    }

    @ApiOperation(value = "프로필 업데이트")
    @PutMapping("/update-profile")
    private HttpStatus updateProfile(
            @RequestParam(value = "userId") Long id,
            @RequestParam(value = "picture") MultipartFile picture
    ) {
        return myPageService.updateProfile(id, picture);
    }

    @ApiOperation(value = "닉네임 업데이트")
    @PutMapping("/update-nickname")
    private HttpStatus updateProfile(
            @RequestParam(value = "userId") Long id,
            @RequestParam(value = "nickname") String nickname
    ) {
        return myPageService.updateNickname(id, nickname);
    }

}
