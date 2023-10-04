package com.anpatapain.coffwok;

import com.anpatapain.coffwok.common.config.AppProperties;
import com.anpatapain.coffwok.image_upload.service.ImageStorageService;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@EnableAspectJAutoProxy
public class CoffwokApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoffwokApplication.class, args);
    }
}
