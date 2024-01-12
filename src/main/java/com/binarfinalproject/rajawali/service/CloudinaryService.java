package com.binarfinalproject.rajawali.service;

import org.springframework.web.multipart.MultipartFile;

//this service for converting the image into URL
public interface CloudinaryService {
    public String uploadFile(MultipartFile file, String folderName);
}
