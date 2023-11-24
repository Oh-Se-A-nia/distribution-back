package com.ohseania.ecotag.domain.crawlingVO.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class News {

    private String url;
    private String href;
    private String alt;

}
