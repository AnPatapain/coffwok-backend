package com.anpatapain.coffwok;

import com.anpatapain.coffwok.common.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class CoffwokApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoffwokApplication.class, args);
    }

}
