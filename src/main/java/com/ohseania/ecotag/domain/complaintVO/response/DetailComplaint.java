package com.ohseania.ecotag.domain.complaintVO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class DetailComplaint {

    private String date;
    private String time;
    private String processType;
    private List<String> complaintDetails;
    private Long cumulativeCount;
    private String ecotagType;
    private String location;
    private List<String> photoUrls;
    private String regionName;

}
