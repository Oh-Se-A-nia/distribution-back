package com.ohseania.ecotag.controller;

import com.ohseania.ecotag.domain.complaintVO.request.ComplaintForm;
import com.ohseania.ecotag.domain.complaintVO.response.DetailComplaint;
import com.ohseania.ecotag.domain.complaintVO.response.EntryComplaint;
import com.ohseania.ecotag.domain.ecotagVO.request.EcotagForm;
import com.ohseania.ecotag.service.complaint.ComplaintService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.List;

@Api(tags = {"Complaint API"}, description = "민원 서비스")
@RestController
@AllArgsConstructor
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    @ApiOperation(value = "민원 신청 API")
    @PostMapping("/post")
    public ResponseEntity postComplaints(@RequestParam(value = "picture") MultipartFile picture,
                                         @RequestParam(value = "latitude") String latitude,
                                         @RequestParam(value = "location") String location,
                                         @RequestParam(value = "longitude") String longitude,
                                         @RequestParam(value = "type") String type,
                                         @RequestParam(value = "userId") String userId,
                                         @RequestParam(value = "postDetail") String postDetail
    ) {
        System.out.println(longitude);
        EcotagForm ecotagForm = EcotagForm.builder()
                .type(type)
                .picture(picture)
                .location(location)
                .latitude(latitude)
                .longitude(longitude)
                .userId(userId)
                .build();
        ComplaintForm complaintForm = ComplaintForm.builder()
                .ecotag(ecotagForm)
                .postDetail(postDetail)
                .build();

        return complaintService.postComplaint(complaintForm);
    }

    @ApiOperation(value = "민원 리스트 제공")
    @GetMapping("/list")
    public ResponseEntity<List<EntryComplaint>> getComplaintList(
            @RequestParam(value = "ecotagType" , required = false) String ecotagType,
            @RequestParam(value = "regionName" , required = false) String regionName,
            @RequestParam(value = "processType" , required = false) String processType
    ) {
        return complaintService.findAllComplaint(ecotagType, regionName, processType);
    }

    @ApiOperation(value = "민원의 처리 결과 수정")
    @PutMapping("/modify-process")
    public ResponseEntity modifyProcessType(
            @RequestParam(value = "complaintId") Long complaintId,
            @RequestParam(value = "processType") String processType
    ) {
        return complaintService.updateProcessType(complaintId, processType);
    }

    @ApiOperation(value = "민원의 상세 내용 조회")
    @GetMapping("/detail")
    public ResponseEntity<DetailComplaint> returnDetailComplaint(
            @RequestParam(value = "complaintId") Long complaintId
    ) {
        return complaintService.findDetailComplaint(complaintId);
    }

    @ApiOperation(value = "일주일 간 누적 민원 수 제공")
    @GetMapping("/cumulative")
    public ResponseEntity<LinkedHashMap<Integer, Integer>> getCumulativeComplaintCount() {
        return complaintService.findRecentComplaints();
    }

}
