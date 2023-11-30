package com.ohseania.ecotag.repository;

import com.ohseania.ecotag.entity.Complaint;
import com.ohseania.ecotag.entity.Ecotag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    List<Complaint> findByUserId(Long id);

    List<Complaint> findByProcessType(String processType);

    Complaint findByEcotagId(Long Id);

    @Query(value = "SELECT * FROM complaint WHERE date >= :sevenDaysAgo",
            nativeQuery = true)
    List<Complaint> findByDateAfter(Date sevenDaysAgo);

}
