package com.anpatapain.coffwok.file_upload.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class FileStorageServiceImpl implements FileStorageService{
    private final Path root = Paths.get("uploads");


    @Autowired
    private Cloudinary cloudinaryConfig;

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        }catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for uploading");
        }
    }

    @Override
    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        }catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("File of that name already existed");
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String saveToCloudinary(MultipartFile file) {
        Map<?, ?> params = ObjectUtils.asMap(
                "public_id", "coffwok_dev/" + StringUtils.cleanPath(file.getOriginalFilename())
        );
        try{
            Map<?, ?> uploadResult = cloudinaryConfig.uploader().upload(file.getBytes(), params);
            return (String) uploadResult.get("secure_url");
        }catch (Exception e) {
            throw new RuntimeException("Can not upload to cloudinary");
        }
    }

    @Override
    public Resource load(String filename) {
        return null;
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }
}
