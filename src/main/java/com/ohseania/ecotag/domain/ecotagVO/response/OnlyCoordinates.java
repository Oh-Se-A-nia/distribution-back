package com.ohseania.ecotag.domain.ecotagVO.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OnlyCoordinates {

    private Double latitude;
    private Double longitude;

}
