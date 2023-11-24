package com.ohseania.ecotag.service.cleaning;

import com.ohseania.ecotag.domain.cleaningCompany.request.CompanyRegistration;
import com.ohseania.ecotag.entity.CleaningCompany;
import com.ohseania.ecotag.repository.CleaningCompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CleaningCompanyServiceImpl implements CleaningCompanyService {

    private final CleaningCompanyRepository cleaningCompanyRepository;

    @Override
    public HttpStatus registCompany(CompanyRegistration companyRegistration) {
        cleaningCompanyRepository.save(CleaningCompany.builder()
                .name(companyRegistration.getName())
                .callNumber(companyRegistration.getCallNumber())
                .memo(companyRegistration.getMemo())
                .build());

        return HttpStatus.OK;
    }

    @Override
    public ResponseEntity<List<CleaningCompany>> checkCleaningCompanys() {
        try {
            return new ResponseEntity<>(cleaningCompanyRepository.findAll(), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
