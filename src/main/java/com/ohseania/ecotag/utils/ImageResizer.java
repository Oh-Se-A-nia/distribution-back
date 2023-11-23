package com.ohseania.ecotag.utils;

import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ImageResizer {

    private static final int width = 340;
    private static final int height = 300;

    public String resizeImage(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);

        File file = File.createTempFile("resized_image_", ".jpg");

        Thumbnails.of(url)
                .size(width, height)
                .outputFormat("jpg")
                .toFile(file);

        return file.toURI().toURL().toString();
    }

}
