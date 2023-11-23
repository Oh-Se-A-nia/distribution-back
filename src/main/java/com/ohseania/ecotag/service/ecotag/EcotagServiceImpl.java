package com.ohseania.ecotag.service.ecotag;

import com.ohseania.ecotag.domain.ecotagVO.request.EcotagForm;
import com.ohseania.ecotag.domain.ecotagVO.response.EcoTypeCountInterface;
import com.ohseania.ecotag.domain.ecotagVO.response.EcotagCoordinate;
import com.ohseania.ecotag.entity.*;
import com.ohseania.ecotag.repository.ComplaintRepository;
import com.ohseania.ecotag.repository.EcotagRepository;
import com.ohseania.ecotag.repository.PhotoRepository;
import com.ohseania.ecotag.repository.UserRepository;
import com.ohseania.ecotag.service.region.RegionService;
import com.ohseania.ecotag.service.s3.S3Service;
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
@RequiredArgsConstructor
public class EcotagServiceImpl implements EcotagService {

    private final EcotagRepository ecotagRepository;
    private final UserRepository userRepository;
    private final ComplaintRepository complaintRepository;
    private final PhotoRepository photoRepository;

    private final RegionService regionService;
    private final S3Service s3Service;

    @Transactional(noRollbackFor = RuntimeException.class)
    @Override
    public HttpStatus uploadEcotag(EcotagForm ecotagForm) {
        Ecotag ecotag;
        try {
            Region region = regionService.formatRegion(ecotagForm.getLocation());
            ecotag = createEcotag(ecotagForm, region);
            ecotagRepository.save(ecotag);
            setPhotofile(ecotagForm);

            return HttpStatus.OK;
        } catch (IllegalArgumentException e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public Ecotag createEcotag(EcotagForm ecotagForm, Region region) {
        updateCumulativeCount(Long.parseLong(ecotagForm.getUserId()));

        return Ecotag.builder()
                .region(region)
                .user(userRepository.findById(Long.parseLong(ecotagForm.getUserId())).get())
                .ecotagType(ecotagForm.getType())
                .latitude(Double.valueOf(ecotagForm.getLatitude()))
                .longitude(Double.valueOf(ecotagForm.getLongitude()))
                .location(ecotagForm.getLocation())
                .build();
    }

    private void setPhotofile(EcotagForm ecotagForm) {
        Photo photo = s3Service.uploadMedia(ecotagForm.getPicture());
        photoRepository.save(photo);
    }

    private void updateCumulativeCount(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new IllegalArgumentException();
        }

        userRepository.save(User.builder()
                .id(id)
                .photo(user.get().getPhoto())
                .nickname(user.get().getNickname())
                .cumulativeAction(user.get().getCumulativeAction() + 1)
                .build()
        );
    }

    @Override
    public List<EcoTypeCountInterface> trackMostTrash() {
        return ecotagRepository.findByMostTrash();
    }

    @Override
    public Ecotag updateCumulativeCount(Ecotag originEcotag) {
        return Ecotag.builder()
                .cumulativeCount(originEcotag.getCumulativeCount() + 1)
                .ecotagType(originEcotag.getEcotagType())
                .latitude(originEcotag.getLatitude())
                .longitude(originEcotag.getLongitude())
                .region(originEcotag.getRegion())
                .user(originEcotag.getUser())
                .location(originEcotag.getLocation())
                .id(originEcotag.getId())
                .build();
    }

    @Override
    public ResponseEntity<List<EcotagCoordinate>> getCoordinate() {
        try {
            List<EcotagCoordinate> coordinates = complaintCoordinate();
            return new ResponseEntity<>(coordinates, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private List<EcotagCoordinate> complaintCoordinate() {
        List<EcotagCoordinate> ecotagCoordinates = new ArrayList<>();
        List<Complaint> complaints = complaintRepository.findAll();
        List<Long> ecotagId = new ArrayList<>();

        for (Complaint complaint : complaints) {
            ecotagId.add(complaint.getEcotag().getId());

            ecotagCoordinates.add(EcotagCoordinate.builder()
                    .isComplaint(true)
                    .ecotagType(complaint.getEcotag().getEcotagType())
                    .longitude(complaint.getEcotag().getLongitude())
                    .latitude(complaint.getEcotag().getLatitude())
                    .build());
        }

        return ecotagCoordinate(ecotagCoordinates, ecotagId);
    }

    private List<EcotagCoordinate> ecotagCoordinate(List<EcotagCoordinate> ecotagCoordinates, List<Long> ecotagId) {
        List<Ecotag> ecotags = ecotagRepository.findAll();

        for (Ecotag ecotag : ecotags) {
            if (ecotagId.contains(ecotag.getId())) {
                continue;
            }

            ecotagCoordinates.add(EcotagCoordinate.builder()
                    .isComplaint(false)
                    .ecotagType(ecotag.getEcotagType())
                    .latitude(ecotag.getLatitude())
                    .longitude(ecotag.getLongitude())
                    .build());
        }

        return ecotagCoordinates;
    }

}
