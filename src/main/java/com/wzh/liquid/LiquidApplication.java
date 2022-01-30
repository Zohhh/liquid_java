package com.wzh.liquid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.net.ServerSocket;

@SpringBootApplication
public class LiquidApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiquidApplication.class, args);
    }
    @Bean
    public ServerSocket serverSocket() throws IOException {
        return new ServerSocket(8234);
    }
}
