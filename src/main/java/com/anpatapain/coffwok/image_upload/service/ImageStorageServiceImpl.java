package com.anpatapain.coffwok.image_upload.service;

import com.anpatapain.coffwok.image_upload.exception.ImageUploadException;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ImageStorageServiceImpl implements ImageStorageService {

    @Autowired
    private Cloudinary cloudinaryConfig;

    @Override
    public String saveToCloudinary(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = StringUtils.getFilenameExtension(originalFilename);
        String uniqueFileName = UUID.randomUUID().toString() + "." + extension;
        String imagePublicId = "coffwok_dev/" + uniqueFileName;

        Map<?, ?> params = ObjectUtils.asMap("public_id", imagePublicId);

        try{
            Map<?, ?> uploadResult = cloudinaryConfig.uploader().upload(file.getBytes(), params);
            return (String) uploadResult.get("secure_url");
        }catch (Exception e) {
            throw new ImageUploadException();
        }
    }
}
