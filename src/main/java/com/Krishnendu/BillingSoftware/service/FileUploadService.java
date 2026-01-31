package com.Krishnendu.BillingSoftware.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    String uploadFile(MultipartFile file);

    Boolean deleteFile(String imageUrl);
}
