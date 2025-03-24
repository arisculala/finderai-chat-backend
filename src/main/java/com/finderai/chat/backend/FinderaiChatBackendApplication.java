package com.finderai.chat.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info = @Info(title = "FinderAI Chat Service API", version = "0.0.1", description = "API for FinderAI Chat Service"))
@SpringBootApplication
public class FinderaiChatBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinderaiChatBackendApplication.class, args);
    }

}
