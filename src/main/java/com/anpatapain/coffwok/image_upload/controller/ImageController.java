package com.anpatapain.coffwok.image_upload.controller;

import com.anpatapain.coffwok.common.payload.response.ApiResponse;
import com.anpatapain.coffwok.image_upload.service.ImageStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ImageController {
    private ImageStorageService storageService;
    private Logger logger = LoggerFactory.getLogger(ImageController.class);
    @Autowired
    public ImageController(ImageStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/api/files/upload")
    public ResponseEntity<ApiResponse> uploadFile(@RequestParam("file")MultipartFile file) {
        String message = "";
        try {
            String url = storageService.saveToCloudinary(file);
            logger.info("cloudinary url: " + url);
            message = "Uploaded file successfully " + file.getOriginalFilename();
            return ResponseEntity.ok(new ApiResponse(true, message));
        }catch (Exception e) {
            message = "Could not upload " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ApiResponse(false, message));
        }
    }
}
