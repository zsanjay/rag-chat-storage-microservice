package com.assignment.rag_chat_storage_service.exception;

public class SessionAlreadyExistsException extends RuntimeException {
    public SessionAlreadyExistsException(String title) {
        super(String.format("Session already exists for the title: %s", title));
    }
}
