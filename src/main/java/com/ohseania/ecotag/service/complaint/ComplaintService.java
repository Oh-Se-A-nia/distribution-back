package com.ohseania.ecotag.service.complaint;

import com.ohseania.ecotag.domain.complaintVO.request.ComplaintForm;
import com.ohseania.ecotag.domain.complaintVO.response.DetailComplaint;
import com.ohseania.ecotag.domain.complaintVO.response.EntryComplaint;
import com.ohseania.ecotag.entity.Complaint;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.List;

public interface ComplaintService {

    ResponseEntity postComplaint(ComplaintForm complaintForm);

    List<Complaint> findUsersComplaint(Long id);

    ResponseEntity<List<EntryComplaint>> findAllComplaint(String ecotagType, String region, String processType);

    ResponseEntity updateProcessType(Long complaintId, String processTypeToChange);

    ResponseEntity<DetailComplaint> findDetailComplaint(Long complaintId);

    ResponseEntity<LinkedHashMap<Integer, Integer>> findRecentComplaints();

}
