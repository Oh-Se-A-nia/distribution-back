package com.ohseania.ecotag.service.myPage;

import com.ohseania.ecotag.domain.complaintVO.response.MyComplaint;
import com.ohseania.ecotag.entity.Complaint;
import com.ohseania.ecotag.entity.MyPage;
import com.ohseania.ecotag.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MyPageService {

    ResponseEntity<List<MyComplaint>> returnMyComplaintList(Long userId);

    HttpStatus updateProfile(Long userId, MultipartFile profile);

    HttpStatus updateNickname(Long userId, String nickname);

}
