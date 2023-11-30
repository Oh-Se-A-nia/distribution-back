package com.ohseania.ecotag.service.user;

import com.ohseania.ecotag.domain.userVO.request.LogIn;
import com.ohseania.ecotag.domain.userVO.request.SignUpForm;
import com.ohseania.ecotag.domain.userVO.response.UserInformation;
import com.ohseania.ecotag.entity.Admin;
import com.ohseania.ecotag.entity.Photo;
import com.ohseania.ecotag.entity.User;
import com.ohseania.ecotag.repository.AdminRepository;
import com.ohseania.ecotag.repository.PhotoRepository;
import com.ohseania.ecotag.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public ResponseEntity<UserInformation> validateUserInformaion(SignUpForm signUpForm) {
        Optional<User> signedUpUser = userRepository.findById(signUpForm.getId());

        if (signedUpUser.isPresent()) {
            User existingUser = signedUpUser.get();

            return new ResponseEntity<>(new UserInformation(existingUser.getId()), HttpStatus.OK);
        }

        return saveUserInformation(signUpForm);
    }

    @Override
    public HttpStatus loginAdmin(LogIn login) {
        Optional<Admin> admin = adminRepository.findById(login.getId());

        boolean isValid = BCrypt.checkpw(login.getPassword(), admin.get().getPassword());

        if (isValid) {
            return HttpStatus.OK;
        }

        return HttpStatus.BAD_REQUEST;
    }

    private ResponseEntity<UserInformation> saveUserInformation(SignUpForm signUpForm) {
        Photo photo = photoRepository.findByType("profile");

        User user = User.builder()
                .id(signUpForm.getId())
                .nickname(signUpForm.getNickname())
                .photo(photo)
                .build();

        User save = userRepository.save(user);

        return new ResponseEntity<>(new UserInformation(signUpForm.getId()), HttpStatus.OK);
    }
}
