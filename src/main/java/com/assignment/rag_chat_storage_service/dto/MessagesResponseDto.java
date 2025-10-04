package com.assignment.rag_chat_storage_service.dto;

import lombok.Builder;

import java.util.List;

public record MessagesResponseDto(Long sessionId, String userId, List<MessageResponse> messages) {

    @Builder
    public MessagesResponseDto {}
}
