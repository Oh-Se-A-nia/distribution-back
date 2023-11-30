package com.ohseania.ecotag.domain.complaintVO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class MyComplaint {

    private List<String> url;
    private String regionName;
    private String processType;
    private String ecotagType;

}
