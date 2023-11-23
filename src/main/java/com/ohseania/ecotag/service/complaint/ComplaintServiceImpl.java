package com.ohseania.ecotag.service.complaint;

import com.ohseania.ecotag.entity.Ecotag;
import com.ohseania.ecotag.domain.complaintVO.request.ComplaintForm;
import com.ohseania.ecotag.domain.complaintVO.response.DetailComplaint;
import com.ohseania.ecotag.domain.complaintVO.response.EntryComplaint;
import com.ohseania.ecotag.entity.*;
import com.ohseania.ecotag.repository.*;
import com.ohseania.ecotag.service.ecotag.EcotagService;
import com.ohseania.ecotag.service.region.RegionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final ComplaintDetailRepository complaintDetailRepository;

    private final EcotagService ecotagService;
    private final RegionService regionService;
    private final EcotagRepository ecotagRepository;

    @Override
    public ResponseEntity postComplaint(ComplaintForm complaintForm) {
        try {
            Region region = regionService.formatRegion(complaintForm.getEcotag().getLocation());
            Ecotag newEcotag = ecotagService.createEcotag(complaintForm.getEcotag(), region);
            User writer = findUser(complaintForm.getEcotag().getUserId());

            Ecotag tmpEcotag = isEqualPost(complaintForm.getEcotag().getLatitude(), complaintForm.getEcotag().getLongitude());

            if (tmpEcotag != null) {
                newEcotag = ecotagService.updateCumulativeCount(tmpEcotag);

            }

            Complaint complaint = complaintRepository.save(Complaint.builder()
                    .ecotag(newEcotag)
                    .user(writer)
                    .build());

            complaintDetailRepository.save(createComplaintDetail(complaintForm.getPostDetail(), complaint));

        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    public List<Complaint> findUsersComplaint(Long id) {
        return complaintRepository.findByUserId(id);
    }

    @Override
    public ResponseEntity<List<EntryComplaint>> findAllComplaint(String ecotagType, String region, String processType) {
        List<Complaint> complaints = judgeComplaintType(processType);

        complaints = judgeEcotagType(complaints, ecotagType);
        complaints = judgeRegion(complaints, region);

        return new ResponseEntity<>(changeForm(complaints), HttpStatus.OK);
    }

    @Override
    public ResponseEntity updateProcessType(Long complaintId, String processTypeToChange) {
        Optional<Complaint> complaint = complaintRepository.findById(complaintId);

        if (complaint.isPresent()) {
            complaintRepository.save(Complaint.builder()
                    .processType(processTypeToChange)
                    .ecotag(complaint.get().getEcotag())
                    .creationDate(complaint.get().getCreationDate())
                    .creationTime(complaint.get().getCreationTime())
                    .user(complaint.get().getUser())
                    .id(complaintId)
                    .build());

            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<DetailComplaint> findDetailComplaint(Long complaintId) {
        Optional<Complaint> complaint = complaintRepository.findById(complaintId);

        if (complaint.isPresent()) {
            List<String> photoUrls = getPhotoUrl(complaint.get().getEcotag().getId());
            List<String> complaintDetails = getComplaintDetail(complaintId);
            System.out.println(3);

            DetailComplaint detailComplaint = DetailComplaint.builder()
                    .cumulativeCount(complaint.get().getEcotag().getCumulativeCount())
                    .location(complaint.get().getEcotag().getLocation())
                    .time(complaint.get().getCreationTime().toString())
                    .date(complaint.get().getCreationDate().toString())
                    .processType(complaint.get().getProcessType())
                    .regionName(complaint.get().getEcotag().getRegion().getRegionName())
                    .ecotagType(complaint.get().getEcotag().getEcotagType())
                    .photoUrls(photoUrls)
                    .complaintDetails(complaintDetails)
                    .latitude(complaint.get().getEcotag().getLatitude())
                    .longitude(complaint.get().getEcotag().getLongitude())
                    .build();

            System.out.println(4);
            return new ResponseEntity<>(detailComplaint, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private List<String> getPhotoUrl(Long ecotagId) {
        List<Photo> photos = photoRepository.findByEcotagId(ecotagId);

        List<String> urls = new ArrayList<>();

        for (Photo photo : photos) {
            urls.add(photo.getUrl());
        }

        return urls;
    }

    private List<String> getComplaintDetail(Long complaintId) {
        List<ComplaintDetail> complaintDetails = complaintDetailRepository.findByComplaintId(complaintId);
        List<String> details = new ArrayList<>();

        for (ComplaintDetail complaintDetail : complaintDetails) {
            details.add(complaintDetail.getDetail());
        }

        return details;
    }

    private List<Complaint> judgeComplaintType(String processType) {
        if (processType == null) {
            return complaintRepository.findAll();
        }
        return complaintRepository.findByProcessType(processType);
    }

    private List<Complaint> judgeEcotagType(List<Complaint> originComplaint, String ecotagType) {
        if (ecotagType == null) {
            return originComplaint;
        }

        for (Complaint complaint : originComplaint) {
            if (complaint.getEcotag().getEcotagType() != ecotagType) {
                originComplaint.remove(complaint);
            }
        }

        return originComplaint;
    }

    private List<Complaint> judgeRegion(List<Complaint> originComplaint, String region) {
        if (region == null) {
            return originComplaint;
        }

        for (Complaint complaint : originComplaint) {
            if (complaint.getEcotag().getRegion().getRegionName() != region) {
                originComplaint.remove(complaint);
            }
        }

        return originComplaint;
    }

    private List<EntryComplaint> changeForm(List<Complaint> originComplaint) {
        List<EntryComplaint> totalComplaints = new ArrayList<>();

        for (Complaint complaint : originComplaint) {
            totalComplaints.add(EntryComplaint.builder()
                    .id(complaint.getId())
                    .date(complaint.getCreationDate().toString())
                    .time(complaint.getCreationTime().toString())
                    .processType(complaint.getProcessType())
                    .regionName(complaint.getEcotag().getRegion().getRegionName())
                    .ecotagType(complaint.getEcotag().getEcotagType())
                    .build());
        }

        return totalComplaints;
    }

    private Ecotag isEqualPost(String latitudeValue, String longitudeValue) {
        Double latitude = Double.valueOf(latitudeValue);
        Double longitude = Double.valueOf(longitudeValue);
        List<Ecotag> equalEcotag = ecotagRepository.findByLatitudeAndLongitude(latitude, longitude);

        if (equalEcotag.size() > 0) {
            return equalEcotag.get(0);
        }
        return null;
    }

    private User findUser(String userId) {
        Long id = Long.parseLong(userId);
        return userRepository.findById(id).get();
    }

    private ComplaintDetail createComplaintDetail(String detail, Complaint complaint) {
        return ComplaintDetail.builder()
                .complaint(complaint)
                .detail(detail)
                .build();
    }

}
