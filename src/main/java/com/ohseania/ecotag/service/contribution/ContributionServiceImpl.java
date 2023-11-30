package com.ohseania.ecotag.service.contribution;

import com.ohseania.ecotag.domain.contributionVO.response.MyContribution;
import com.ohseania.ecotag.domain.contributionVO.response.Rank;
import com.ohseania.ecotag.domain.userVO.response.UserRank;
import com.ohseania.ecotag.entity.User;
import com.ohseania.ecotag.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ContributionServiceImpl implements ContributionService {

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<List<Rank>> getRanking() {
        try {
            List<UserRank> rankers = userRepository.findTopCumulativeAction(5);
            List<Rank> users = new ArrayList<>();

            for (UserRank user : rankers) {
                User rankingUser = userRepository.findById(user.getId()).get();

                users.add(Rank.builder()
                        .nickname(rankingUser.getNickname())
                        .cumulativeCount(rankingUser.getCumulativeAction())
                        .profileUrl(rankingUser.getPhoto().getUrl())
                        .build());
            }

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<MyContribution> getContribution(Long userId) {
        try {
            Long totalEcotag = findTotalEcotag();
            Long myEcotag = findMyEcotag(userId);

            MyContribution myContribution = MyContribution.builder()
                    .myEcotag(myEcotag)
                    .totalEcotag(totalEcotag)
                    .build();

            return new ResponseEntity<>(myContribution, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private Long findTotalEcotag() {
        Long totalEcotag = 0L;

        List<User> users = userRepository.findAll();

        for (User user : users) {
            totalEcotag += user.getCumulativeAction();
        }

        return totalEcotag;
    }

    private Long findMyEcotag(Long id) {
        try {
            User user = userRepository.findById(id).get();
            return user.getCumulativeAction();
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

}
