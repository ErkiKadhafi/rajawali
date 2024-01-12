package com.binarfinalproject.rajawali.service;

import java.util.Map;

import com.binarfinalproject.rajawali.dto.image.ImageModel;
import org.springframework.http.ResponseEntity;

public interface ImageService {
    public ResponseEntity<Map> uploadImage(ImageModel imageModel);
}
