package com.ohseania.ecotag.domain.complaintVO.request;

import com.ohseania.ecotag.domain.ecotagVO.request.EcotagForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ComplaintForm {

    @ApiModelProperty(value = "민원 대상인 쓰레기")
    private EcotagForm ecotag;

    @ApiModelProperty(value = "민원 내용")
    private String postDetail;

}
