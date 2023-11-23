package com.ohseania.ecotag.service.user;

import com.ohseania.ecotag.domain.userVO.request.SignUpForm;
import com.ohseania.ecotag.domain.userVO.response.UserInformation;
import com.ohseania.ecotag.entity.Contribution;
import com.ohseania.ecotag.entity.Photo;
import com.ohseania.ecotag.entity.User;
import com.ohseania.ecotag.repository.ContributionRepository;
import com.ohseania.ecotag.repository.PhotoRepository;
import com.ohseania.ecotag.repository.UserRepository;
import com.ohseania.ecotag.service.contribution.ContributionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ContributionService contributionService;
    private final PhotoRepository photoRepository;

    @Override
    public ResponseEntity<UserInformation> validateUserInformaion(SignUpForm signUpForm) {
        Optional<User> signedUpUser = userRepository.findById(signUpForm.getId());

        if (signedUpUser.isPresent()) {
            User existingUser = signedUpUser.get();

            return new ResponseEntity<>(new UserInformation(existingUser.getId()), HttpStatus.OK);
        }

        return saveUserInformation(signUpForm);
    }

    private ResponseEntity<UserInformation> saveUserInformation(SignUpForm signUpForm) {
        Photo photo = photoRepository.findByType("profile");
        System.out.println(photo.getUrl());

        User user = User.builder()
                .id(signUpForm.getId())
                .nickname(signUpForm.getNickname())
                .photo(photo)
                .build();

        User save = userRepository.save(user);
        contributionService.createContributionForm(save);

        return new ResponseEntity<>(new UserInformation(signUpForm.getId()), HttpStatus.OK);
    }
}
