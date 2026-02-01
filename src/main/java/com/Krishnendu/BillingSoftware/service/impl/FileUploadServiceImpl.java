package com.Krishnendu.BillingSoftware.service.impl;

import com.Krishnendu.BillingSoftware.service.FileUploadService;
import com.Krishnendu.BillingSoftware.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${aws.bucket.name}")
    private String bucketName;
    @Value("${aws.region}")
    private String region;

    private final S3Client s3Client;

    @Override
    public String uploadFile(MultipartFile file) {
        String filenameExtension = FileUtils.getFileExtensionFromMultipartFile(file);
        String key = UUID.randomUUID().toString() + "." + filenameExtension;

        try{
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key) // filename basically
                    .acl("public-read")
                    .contentType(file.getContentType())
                    .build();

            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            if(response.sdkHttpResponse().isSuccessful()) {
                String url = "https://" + bucketName + ".s3." + region +".amazonaws.com/" + key;
                return url;
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while uploading the image");
            }

        }catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while uploading the file");
        }
    }

    @Override
    public Boolean deleteFile(String imageUrl) {
        String fileName = FileUtils.getFilenameFromURL(imageUrl);

        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();


        s3Client.deleteObject(deleteRequest);
        return true;
    }


}

