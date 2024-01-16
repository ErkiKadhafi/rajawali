package com.binarfinalproject.rajawali.service.impl;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.binarfinalproject.rajawali.dto.image.request.UploadImageDto;
import com.binarfinalproject.rajawali.dto.image.response.ResImageDto;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.service.ImageService;
import com.cloudinary.Cloudinary;

import jakarta.annotation.Resource;

@Service
public class ImageServiceImpl implements ImageService {
    @Value("${cloudinary.folder_name}")
    private String folderName;

    @Resource
    private Cloudinary cloudinary;

    @Override
    public ResImageDto uploadImage(UploadImageDto request) throws ApiException {
        try {
            if(request.getFile().getSize() > 1 * 1024 * 1024){
                throw new ApiException(HttpStatus.BAD_REQUEST, "Max File 1 mb");
            }
            String publicId = (String) cloudinary.uploader()
                    .upload(request.getFile().getBytes(),
                            Map.of("folder", folderName, "public_id", UUID.randomUUID().toString()))
                    .get("public_id");

            ResImageDto resImageDto = new ResImageDto();
            resImageDto.setUrlImage(cloudinary.url().secure(true).generate(publicId));

            return resImageDto;
        } catch (Exception e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
