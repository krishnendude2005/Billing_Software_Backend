package com.Krishnendu.BillingSoftware.utils;

import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
    public static String getFileExtensionFromMultipartFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();

        if (fileName == null || !fileName.contains(".")) {
            return "";
        }

        return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

    }

    public static String getFilenameFromURL(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }
}
