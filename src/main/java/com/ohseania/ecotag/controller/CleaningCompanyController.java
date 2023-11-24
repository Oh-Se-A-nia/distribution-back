package com.ohseania.ecotag.controller;

import com.ohseania.ecotag.domain.cleaningCompany.request.CompanyRegistration;
import com.ohseania.ecotag.domain.complaintVO.response.EntryComplaint;
import com.ohseania.ecotag.entity.CleaningCompany;
import com.ohseania.ecotag.service.cleaning.CleaningCompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"Cleaning-Company API"}, description = "청소 업체 서비스")
@RestController
@AllArgsConstructor
@RequestMapping("/api/cleaning")
public class CleaningCompanyController {

    private final CleaningCompanyService cleaningCompanyService;

    @ApiOperation(value = "업체 등록")
    @PutMapping("/registration")
    public HttpStatus registBusiness(@RequestBody CompanyRegistration companyRegistration) {
        return cleaningCompanyService.registCompany(companyRegistration);
    }

    @ApiOperation(value = "전체 업체 조화")
    @GetMapping("/cleaning-list")
    public ResponseEntity<List<CleaningCompany>> viewCleaningList() {
        return cleaningCompanyService.checkCleaningCompanys();
    }

}
