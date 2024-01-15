package com.binarfinalproject.rajawali.service;

import com.binarfinalproject.rajawali.dto.image.request.UploadImageDto;
import com.binarfinalproject.rajawali.dto.image.response.ResImageDto;
import com.binarfinalproject.rajawali.exception.ApiException;

public interface ImageService {
    ResImageDto uploadImage(UploadImageDto request) throws ApiException;
}
