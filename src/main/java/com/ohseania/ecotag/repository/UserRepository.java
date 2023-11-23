package com.ohseania.ecotag.repository;

import com.ohseania.ecotag.domain.userVO.response.UserRank;
import com.ohseania.ecotag.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(nativeQuery = true,
    value = "SELECT id, RANK() OVER (ORDER BY cumulative_action DESC) as cumulative_action_rank\n" +
            "FROM user\n" +
            "ORDER BY cumulative_action DESC;")
    List<UserRank> findTopCumulativeAction(int limit);

}