package com.ohseania.ecotag.service.user;

import com.ohseania.ecotag.domain.userVO.request.LogIn;
import com.ohseania.ecotag.domain.userVO.request.SignUpForm;
import com.ohseania.ecotag.domain.userVO.response.UserInformation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<UserInformation> validateUserInformaion(SignUpForm signUpForm);

    HttpStatus loginAdmin(LogIn login);

}
