package com.ohseania.ecotag.repository;

import com.ohseania.ecotag.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {

    Region findByRegionName(String name);

}