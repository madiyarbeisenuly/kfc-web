package com.kfc.kfcweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class KfcWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(KfcWebApplication.class, args);
    }
}
