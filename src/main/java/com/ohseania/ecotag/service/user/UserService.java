package com.ohseania.ecotag.service.user;

import com.ohseania.ecotag.domain.userVO.request.SignUpForm;
import com.ohseania.ecotag.domain.userVO.response.UserInformation;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<UserInformation> validateUserInformaion(SignUpForm signUpForm);

}
