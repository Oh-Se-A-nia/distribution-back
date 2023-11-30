package com.ohseania.ecotag.service.crawling;

import com.ohseania.ecotag.domain.crawlingVO.response.Letter;
import com.ohseania.ecotag.domain.crawlingVO.response.News;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class CrawlingServiceImpl implements CrawlingService {

    private static final String letterUrl = "http://news.sd.go.kr/mnews/";
    private static final String letterFrontUrl = "http://news.sd.go.kr";

    private static final String newsUrl = "https://news.naver.com/main/list.naver?mode=LS2D&mid=shm&sid1=102&sid2=252";

    @Override
    public List<Letter> getLetterData() {
        try {
            Document document = Jsoup.connect(letterUrl).get();
            Elements atags = document.select("a[href]");

            return extractLetterImage(atags);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public List<News> getNewsData() {
        try {
            Document document = Jsoup.connect(newsUrl).get();
            Elements ultags = document.select("ul.type06_headline li");

            return extractNewsImage(ultags);
        } catch (IOException e) {
            return null;
        }

    }

    private List<Letter> extractLetterImage(Elements atags) {
        List<Letter> letters = new ArrayList<>();

        for (Element atag : atags) {
            Element imgTag = atag.selectFirst("img");

            if (imgTag != null) {
                String imageUrl = imgTag.attr("src");
                String linkUrl = atag.attr("href");

                if (imageUrl.contains("png")) {
                    continue;
                }

                imageUrl = parseImageUrl(imageUrl);

                letters.add(Letter.builder()
                        .href(linkUrl)
                        .url(imageUrl)
                        .build());
            }
        }

        return letters;
    }

    private String parseImageUrl(String imageUrl) {
        imageUrl = imageUrl.replace("..", "");
        return letterFrontUrl + imageUrl;
    }

    private List<News> extractNewsImage(Elements ultags) {
        List<News> news = new ArrayList<>();
        int count = 0;
        int numberOfArticlesToFetch = 10;

        for (Element element : ultags) {
            Element link = element.select("dt a").first();
            String href = link.attr("href");

            Element img = element.select("dt.photo img").first();
            String src = img.attr("src");
            String alt = img.attr("alt");

            news.add(News.builder()
                    .url(src)
                    .href(href)
                    .alt(alt)
                    .build());

            count++;

            if (count >= numberOfArticlesToFetch) {
                break;
            }
        }

        return news;
    }
}
