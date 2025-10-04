package com.assignment.rag_chat_storage_service.dto;

import lombok.Builder;

import java.util.List;

public record MessageResponseDto(Long sessionId, String userId, MessageResponse message) {

    @Builder
    public MessageResponseDto {}
}
