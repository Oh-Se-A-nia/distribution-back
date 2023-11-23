package com.ohseania.ecotag.service.myPage;

import com.ohseania.ecotag.constant.TrashType;
import com.ohseania.ecotag.domain.complaintVO.response.MyComplaint;
import com.ohseania.ecotag.entity.Complaint;
import com.ohseania.ecotag.entity.Photo;
import com.ohseania.ecotag.entity.User;
import com.ohseania.ecotag.repository.PhotoRepository;
import com.ohseania.ecotag.repository.UserRepository;
import com.ohseania.ecotag.service.complaint.ComplaintService;
import com.ohseania.ecotag.service.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;

    private final ComplaintService complaintService;
    private final S3Service s3Service;

    @Override
    public ResponseEntity<List<MyComplaint>> returnMyComplaintList(Long userId) {
        List<Complaint> complaints = complaintService.findUsersComplaint(userId);
        List<MyComplaint> myComplaint = new ArrayList<>();
        int index = 0;

        for (Complaint complaint : complaints) {
            List<Photo> photo = photoRepository.findByEcotagId(complaint.getEcotag().getId());

            myComplaint.add(MyComplaint.builder()
                    .processType(complaint.getProcessType())
                    .url(photo.get(index++).getUrl())
                    .regionName("#" + complaint.getEcotag().getRegion().getRegionName())
                    .ecotagType(sortEcotagType(complaint.getEcotag().getEcotagType()))
                    .build());
        }

        return new ResponseEntity<>(myComplaint, HttpStatus.OK);
    }

    private String sortEcotagType(String originType) {
        return TrashType.getSortedTypeByType(originType);
    }

    @Override
    public HttpStatus updateProfile(Long userId, MultipartFile file) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            Photo profile = s3Service.uploadMedia(file, null);
            User updatedUser = User.builder()
                    .id(userId)
                    .nickname(user.get().getNickname())
                    .photo(profile)
                    .cumulativeAction(user.get().getCumulativeAction())
                    .build();

            userRepository.save(updatedUser);

            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public HttpStatus updateNickname(Long userId, String nickname) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            User updatedUser = User.builder()
                    .id(userId)
                    .nickname(nickname)
                    .photo(user.get().getPhoto())
                    .cumulativeAction(user.get().getCumulativeAction())
                    .build();

            userRepository.save(updatedUser);

            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

}
