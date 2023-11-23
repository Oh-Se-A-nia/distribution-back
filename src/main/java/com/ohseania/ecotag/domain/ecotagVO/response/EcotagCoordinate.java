package com.ohseania.ecotag.domain.ecotagVO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EcotagCoordinate {

    private boolean isComplaint;
    private Double latitude;
    private Double longitude;
    private String ecotagType;

}
