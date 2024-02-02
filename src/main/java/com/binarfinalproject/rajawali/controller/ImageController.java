package com.binarfinalproject.rajawali.controller;

import com.binarfinalproject.rajawali.dto.image.request.UploadImageDto;
import com.binarfinalproject.rajawali.dto.image.response.ResImageDto;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.service.ImageService;
import com.binarfinalproject.rajawali.util.ResponseMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1/images")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> uploadImages(UploadImageDto request) {
        try {
            ResImageDto image = imageService.uploadImage(request);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "Image has successfully uploaded!",
                    image);
        } catch (ApiException e) {
            return ResponseMapper.generateResponseFailed(
                    e.getStatus(), e.getMessage());
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}