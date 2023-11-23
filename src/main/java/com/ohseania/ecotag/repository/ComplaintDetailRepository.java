package com.ohseania.ecotag.repository;

import com.ohseania.ecotag.entity.ComplaintDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ComplaintDetailRepository extends JpaRepository<ComplaintDetail, Long> {

    List<ComplaintDetail> findByComplaintId(Long complaintId);
}