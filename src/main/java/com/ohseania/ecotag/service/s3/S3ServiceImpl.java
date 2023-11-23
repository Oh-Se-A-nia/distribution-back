package com.ohseania.ecotag.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ohseania.ecotag.entity.Photo;
import com.ohseania.ecotag.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class S3ServiceImpl implements S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private final PhotoRepository photoRepository;
    private final AmazonS3Client amazonS3Client;

    @Override
    public Photo uploadMedia(MultipartFile photo) {
        try {
            String fileName = photo.getOriginalFilename();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(photo.getContentType());
            metadata.setContentLength(photo.getSize());
            amazonS3Client.putObject(bucket, fileName, photo.getInputStream(), metadata);
        } catch (AmazonS3Exception e) {
            System.err.println("Error uploading object to S3: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 접근가능한 URL 가져오기
        String photoUrl = amazonS3.getUrl(bucket, photo.getOriginalFilename()).toString();

        // 동시에 해당 미디어 파일들을 미디어 DB에 이름과 Url 정보 저장.
        // 게시글마다 어떤 미디어 파일들을 포함하고 있는지 파악하기 위함 또는 활용하기 위함.
        Photo uploadMedia = Photo.builder()
                .originFileName(photo.getOriginalFilename())
                .type(photo.getContentType())
                .url(photoUrl)
                .build();

        photoRepository.save(uploadMedia);

        return uploadMedia;
    }

    @Override
    public void deleteFile(String fileName) {

    }

    @Override
    public String getFileExtension(String fileName) {
        return null;
    }
}
