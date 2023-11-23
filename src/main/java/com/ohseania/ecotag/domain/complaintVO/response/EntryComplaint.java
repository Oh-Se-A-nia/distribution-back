package com.ohseania.ecotag.domain.complaintVO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EntryComplaint {

    private Long id;
    private String date;
    private String time;
    private String regionName;
    private String ecotagType;
    private String processType;

}
