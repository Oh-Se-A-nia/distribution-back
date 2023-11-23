package com.ohseania.ecotag.service.contribution;

import com.ohseania.ecotag.domain.contributionVO.response.MyContribution;
import com.ohseania.ecotag.domain.contributionVO.response.Rank;
import com.ohseania.ecotag.domain.userVO.response.UserRank;
import com.ohseania.ecotag.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ContributionService {

    ResponseEntity<List<Rank>> getRanking();

    ResponseEntity<MyContribution> getContribution(Long userId);

    void createContributionForm(User user);

}
