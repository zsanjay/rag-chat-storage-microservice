package com.assignment.rag_chat_storage_service.exception;

public class NoSessionFoundException extends RuntimeException {
    public NoSessionFoundException() {
        super("No session found");
    }
}
