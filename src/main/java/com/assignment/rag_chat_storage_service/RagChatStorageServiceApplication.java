package com.assignment.rag_chat_storage_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RagChatStorageServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RagChatStorageServiceApplication.class, args);
	}
}
