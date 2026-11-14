package com.Krishnendu.BillingSoftware.service.impl;

import com.Krishnendu.BillingSoftware.service.FileUploadService;
import com.Krishnendu.BillingSoftware.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/*
 * OLD AWS S3 IMPLEMENTATION - COMMENTED OUT
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
                    .key(key)
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
*/

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${file.upload.dir:uploads}")
    private String uploadDir;

    @Value("${server.servlet.context-path:/api/v1.0}")
    private String contextPath;

    @Value("${server.port:8080}")
    private String serverPort;

    @Override
    public String uploadFile(MultipartFile file) {
        String filenameExtension = FileUtils.getFileExtensionFromMultipartFile(file);
        String filename = UUID.randomUUID().toString() + "." + filenameExtension;

        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String url = "http://localhost:" + serverPort + contextPath + "/files/" + filename;
            log.info("File uploaded successfully: {}", url);
            return url;

        } catch (IOException e) {
            log.error("Error uploading file: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while uploading the file");
        }
    }

    @Override
    public Boolean deleteFile(String imageUrl) {
        try {
            String filename = FileUtils.getFilenameFromURL(imageUrl);
            Path filePath = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(filename);

            Files.deleteIfExists(filePath);
            log.info("File deleted successfully: {}", filename);
            return true;
        } catch (IOException e) {
            log.error("Error deleting file: {}", e.getMessage());
            return false;
        }
    }
}

