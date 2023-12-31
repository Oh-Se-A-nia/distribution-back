package com.ohseania.ecotag.service.s3;

import com.ohseania.ecotag.entity.Ecotag;
import com.ohseania.ecotag.entity.Photo;
import org.springframework.web.multipart.MultipartFile;


public interface S3Service {

    Photo uploadMedia(MultipartFile media, Ecotag ecotag);

    void deleteFile(String fileName);

    String getFileExtension(String fileName);

}
