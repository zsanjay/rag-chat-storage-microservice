package com.assignment.rag_chat_storage_service.exception;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(Long sessionId) {
        super("No Message not found for session id: " + sessionId);
    }
}