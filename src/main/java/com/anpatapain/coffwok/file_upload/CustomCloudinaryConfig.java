package com.anpatapain.coffwok.file_upload;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CustomCloudinaryConfig {
    @Bean
    public Cloudinary cloudinaryConfig() {
        Cloudinary cloudinary = null;
        Map config = new HashMap();
        config.put("cloud_name", "du7mbtyzp");
        config.put("api_key", "743583824276585");
        config.put("api_secret", "3Z6GV9KDJZpq5itODUpYPwO6EDk");
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }
}
