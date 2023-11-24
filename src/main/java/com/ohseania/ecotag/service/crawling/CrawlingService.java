package com.ohseania.ecotag.service.crawling;

import com.ohseania.ecotag.domain.crawlingVO.response.Letter;
import com.ohseania.ecotag.domain.crawlingVO.response.News;

import java.util.List;

public interface CrawlingService {

    List<Letter> getLetterData();

    List<News> getNewsData();

}
