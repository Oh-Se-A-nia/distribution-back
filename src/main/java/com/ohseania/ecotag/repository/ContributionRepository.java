package com.ohseania.ecotag.repository;

import com.ohseania.ecotag.entity.Contribution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContributionRepository extends JpaRepository<Contribution, Long> {
}
