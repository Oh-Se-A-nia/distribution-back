package com.ohseania.ecotag.controller;

import com.ohseania.ecotag.service.crawling.CrawlingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"Crawling API"}, description = "크롤링 서비스")
@RestController
@AllArgsConstructor
@RequestMapping("/api/crawling")
public class CrawlingController {

    private final CrawlingService crawlingService;

    @ApiOperation(value = "성동레터 크롤링 정보 제공 API")
    @GetMapping("/letter")
    private ResponseEntity getLetterInformation() {
        return new ResponseEntity(crawlingService.getLetterData(), HttpStatus.OK);
    }

    @ApiOperation(value = "환경 뉴스 제공 API")
    @GetMapping("/news")
    private ResponseEntity getNewInformation() {
        return new ResponseEntity(crawlingService.getNewsData(), HttpStatus.OK);
    }

}
