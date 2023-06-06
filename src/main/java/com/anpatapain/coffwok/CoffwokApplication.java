package com.anpatapain.coffwok;

import com.anpatapain.coffwok.common.config.AppProperties;
import com.anpatapain.coffwok.image_upload.service.ImageStorageService;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class CoffwokApplication implements CommandLineRunner {
    @Resource
    ImageStorageService storageService;

    public static void main(String[] args) {
        SpringApplication.run(CoffwokApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        storageService.init();
    }
}
