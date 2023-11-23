package com.ohseania.ecotag.domain.contributionVO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyContribution {

    private Long totalEcotag;
    private Long myEcotag;

}
