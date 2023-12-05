package com.example.photos;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan(value={"com.example.photos.mapper"})
public class PhotosApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhotosApplication.class, args);
    }

}
