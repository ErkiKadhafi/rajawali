package com.binarfinalproject.rajawali.dto.image.request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UploadImageDto {
    @NotNull
    private MultipartFile file;
}
