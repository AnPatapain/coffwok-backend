package com.anpatapain.coffwok.image_upload.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface ImageStorageService {
    public String saveToCloudinary(MultipartFile file);
}
