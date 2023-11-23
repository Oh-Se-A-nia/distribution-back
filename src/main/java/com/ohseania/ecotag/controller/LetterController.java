package com.ohseania.ecotag.controller;

import com.ohseania.ecotag.domain.complaintVO.response.MyComplaint;
import com.ohseania.ecotag.service.Letter.LetterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"Sungdong Letter API"}, description = "성동레터 서비스")
@RestController
@AllArgsConstructor
@RequestMapping("/api/letter")
public class LetterController {

    private final LetterService letterService;

    @ApiOperation(value = "성동레터 크롤링 정보 제공 API")
    @GetMapping
    private ResponseEntity serveContributionInfo() {
        return new ResponseEntity(letterService.getLetterDatas(), HttpStatus.OK);
    }

}
