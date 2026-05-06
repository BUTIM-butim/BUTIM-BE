package com.example.butim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ButimApplication {

    public static void main(String[] args) {
        SpringApplication.run(ButimApplication.class, args);
    }

}
