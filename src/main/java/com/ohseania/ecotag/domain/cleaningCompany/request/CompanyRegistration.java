package com.ohseania.ecotag.domain.cleaningCompany.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CompanyRegistration {

    @ApiModelProperty(value = "청소 업체 이름")
    private String name;

    @ApiModelProperty(value = "연락처")
    private String callNumber;

    @ApiModelProperty(value = "특이사항")
    private String memo;

}
