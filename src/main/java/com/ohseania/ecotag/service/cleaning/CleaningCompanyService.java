package com.ohseania.ecotag.service.cleaning;

import com.ohseania.ecotag.domain.cleaningCompany.request.CompanyRegistration;
import com.ohseania.ecotag.entity.CleaningCompany;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CleaningCompanyService {

    HttpStatus registCompany(CompanyRegistration companyRegistration);

    ResponseEntity<List<CompanyRegistration>> checkCleaningCompanys();

}
