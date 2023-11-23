package com.ohseania.ecotag.service.Letter;

import com.ohseania.ecotag.domain.Letter;
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

@Service
@Transactional
@RequiredArgsConstructor
public class LetterServiceImpl implements LetterService {

    private static final String letterUrl = "http://news.sd.go.kr/mnews/";
    private static final String imageFrontUrl = "http://news.sd.go.kr";

    public List<Letter> getLetterDatas() {
        List<Letter> sungdongLetter = extractImage();

        for (Letter letter : sungdongLetter) {
            System.out.println("Image URL: " + letter.getUrl());
            System.out.println("Link URL: " + letter.getHref());
            System.out.println("----------");
        }

        return sungdongLetter;
    }

    private List<Letter> extractImage() {
        List<Letter> imageDateList = new ArrayList<>();

        try {
            Document document = Jsoup.connect(letterUrl).get();
            Elements atags = document.select("a[href]");

            for (Element atag : atags) {
                Element imgTag = atag.selectFirst("img");

                if (imgTag != null) {
                    String imageUrl = imgTag.attr("src");
                    String linkUrl = atag.attr("href");

                    if (imageUrl.contains("png")) {
                        continue;
                    }

                    imageUrl = imageUrl.replace("..", "");
                    imageUrl = imageFrontUrl + imageUrl;

                    imageDateList.add(Letter.builder()
                            .href(linkUrl)
                            .url(imageUrl)
                            .build());
                }
            }

            return imageDateList;

        } catch (IOException e) {
            return null;
        }

    }
}
