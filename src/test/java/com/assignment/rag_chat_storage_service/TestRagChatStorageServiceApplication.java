package com.assignment.rag_chat_storage_service;

import org.springframework.boot.SpringApplication;

public class TestRagChatStorageServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(RagChatStorageServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
