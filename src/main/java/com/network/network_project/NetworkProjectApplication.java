package com.network.network_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NetworkProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(NetworkProjectApplication.class, args);
    }
}
