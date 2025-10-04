package com.assignment.rag_chat_storage_service.exception;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException(Long sessionId) {
        super("Session not found with id: " + sessionId);
    }
}