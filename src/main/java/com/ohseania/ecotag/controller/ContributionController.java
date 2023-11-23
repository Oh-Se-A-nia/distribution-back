package com.ohseania.ecotag.controller;

import com.ohseania.ecotag.domain.contributionVO.response.MyContribution;
import com.ohseania.ecotag.domain.contributionVO.response.Rank;
import com.ohseania.ecotag.service.contribution.ContributionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"Contribution API"}, description = "기여도 서비스")
@RestController
@AllArgsConstructor
@RequestMapping("/api/contribution")
public class ContributionController {

    private final ContributionService contributionService;

    @ApiOperation(value = "기여도 랭킹 제공 API")
    @GetMapping("/rank")
    private ResponseEntity<List<Rank>> serveRanking() {
        return contributionService.getRanking();
    }

    @ApiOperation(value = "나의 기여도 제공 API")
    @GetMapping("/cumulative_count")
    private ResponseEntity<MyContribution> serveContributionInfo(
            @RequestParam(value = "userId") Long id
    ) {
        return contributionService.getContribution(id);
    }

}
