package com.ohseania.ecotag.domain.ecotagVO.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class EcoTypeCount {

    private String regionName;
    private String type;
    private Long trashCount;

}
