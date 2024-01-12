package com.binarfinalproject.rajawali.controller;

import com.binarfinalproject.rajawali.dto.image.ImageModel;
import com.binarfinalproject.rajawali.repository.ImageRepository;
import com.binarfinalproject.rajawali.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/v1/images")
public class ImageController {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<Map> uploadImages(ImageModel imageModel){
        try {
            return imageService.uploadImage(imageModel);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
