package com.fullstack.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Main class for the course price  application.
 */
@EnableFeignClients
@SpringBootApplication
public class CoursePriceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoursePriceApplication.class, args);
    }
}
