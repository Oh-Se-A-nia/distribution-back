package com.ohseania.ecotag.service.complaint;

import com.ohseania.ecotag.entity.Ecotag;
import com.ohseania.ecotag.domain.complaintVO.request.ComplaintForm;
import com.ohseania.ecotag.domain.complaintVO.response.DetailComplaint;
import com.ohseania.ecotag.domain.complaintVO.response.EntryComplaint;
import com.ohseania.ecotag.entity.*;
import com.ohseania.ecotag.repository.*;
import com.ohseania.ecotag.service.ecotag.EcotagService;
import com.ohseania.ecotag.service.region.RegionService;
import com.ohseania.ecotag.service.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final ComplaintDetailRepository complaintDetailRepository;
    private final EcotagRepository ecotagRepository;
    private final MyPageRepository myPageRepository;

    private final EcotagService ecotagService;
    private final RegionService regionService;
    private final S3Service s3Service;

    @Override
    public ResponseEntity postComplaint(ComplaintForm complaintForm) {
        Complaint complaint;
        try {
            Region region = regionService.formatRegion(complaintForm.getEcotag().getLocation());
            Ecotag newEcotag = ecotagService.createEcotag(complaintForm.getEcotag(), region);
            User writer = findUser(complaintForm.getEcotag().getUserId());

            Ecotag tmpEcotag = isEqualPost(complaintForm);

            if (tmpEcotag != null) {
                newEcotag = ecotagService.updateCumulativeCount(tmpEcotag);
            }
            newEcotag = ecotagRepository.save(newEcotag);
            s3Service.uploadMedia(complaintForm.getEcotag().getPicture(), newEcotag);

            if (tmpEcotag != null) {
                complaint = processEqualComplaint(newEcotag);
            } else {
                complaint = processDifferentComplaint(newEcotag, writer);
            }
            complaintDetailRepository.save(createComplaintDetail(complaintForm.getPostDetail(), complaint));
            myPageRepository.save(MyPage.builder()
                    .complaint(complaint)
                    .user(writer)
                    .build());
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
            return new ResponseEntity<>(detailComplaint, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<LinkedHashMap<Integer, Integer>> findRecentComplaints() {
        Date sevenDaysAgo = getSevenDaysAgoDate();
        List<Complaint> currentComplaint = complaintRepository.findByDateAfter(sevenDaysAgo);

        LinkedHashMap<Integer, Integer> complaints = initDateMap();
        complaints = getCumulativeComplaints(currentComplaint);

        return new ResponseEntity<>(complaints, HttpStatus.OK);
    }

    private LinkedHashMap<Integer, Integer> initDateMap() {
        LinkedHashMap<Integer, Integer> linkedHashMap = new LinkedHashMap<>();
        for (int i = 0; i < 7; i++) {
            linkedHashMap.put(-i, 0);
        }

        return linkedHashMap;
    }

    private LinkedHashMap<Integer, Integer> getCumulativeComplaints(List<Complaint> currentComplaints) {
        LinkedHashMap<Integer, Integer> cumulativeComplaint = initDateMap();
        LocalDate now = LocalDate.now();
        Date standard = Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant());

        for (Complaint complaint : currentComplaints) {
            int restDays = Math.toIntExact(getDurationDate(standard, complaint.getCreationDate()));
            int value = cumulativeComplaint.getOrDefault(restDays, 0);
            cumulativeComplaint.put(restDays, value + 1);
        }

        return cumulativeComplaint;
    }

    private Long getDurationDate(Date standardDate, LocalDate targetDate) {
        Instant standard = standardDate.toInstant();
        Instant target = changeDateForm(targetDate).toInstant();

        Duration duration = Duration.between(standard, target);

        return duration.toDays();
    }

    private Date getSevenDaysAgoDate() {
        LocalDate currentDate = LocalDate.now();    // 현재 날짜
        LocalDate sevenDaysAge = currentDate.minusDays(7);  // 7일 이전 날짜

        return changeDateForm(sevenDaysAge);
    }

    private Date changeDateForm(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
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

        List<Complaint> newComplaintList = new ArrayList<>();
        for (Complaint complaint : originComplaint) {
            if (complaint.getEcotag().getEcotagType().equals(ecotagType)) {
                newComplaintList.add(complaint);
            }
        }

        return newComplaintList;
    }

    private List<Complaint> judgeRegion(List<Complaint> originComplaint, String region) {
        if (region == null) {
            return originComplaint;
        }

        List<Complaint> newComplaintList = new ArrayList<>();
        for (Complaint complaint : originComplaint) {
            if (complaint.getEcotag().getRegion().getRegionName().equals(region)) {
                newComplaintList.add(complaint);
            }
        }

        return newComplaintList;
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

    private Ecotag isEqualPost(ComplaintForm complaintForm) {
        List<Ecotag> equalEcotag = ecotagRepository.findByEcotagType(complaintForm.getEcotag().getType());

        for (Ecotag ecotag : equalEcotag) {
            if (isEqualCoordinate(ecotag, complaintForm.getEcotag().getLatitude(), complaintForm.getEcotag().getLongitude())) {
                return ecotag;
            }
        }
        return null;
    }

    private boolean isEqualCoordinate(Ecotag originEcotag, String latitude, String longitude) {
        String originLatitude = String.valueOf(originEcotag.getLatitude());
        String originLogitude = String.valueOf(originEcotag.getLongitude());

        if (Objects.equals(originLatitude, latitude) &&
                Objects.equals(originLogitude, longitude)) {
            return true;
        }
        return false;
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

    private Complaint processEqualComplaint(Ecotag newEcotag) {
        return complaintRepository.findByEcotagId(newEcotag.getId());
    }

    private Complaint processDifferentComplaint(Ecotag newEcotag, User writer) {
        return complaintRepository.save(Complaint.builder()
                .ecotag(newEcotag)
                .user(writer)
                .build());
    }

}
