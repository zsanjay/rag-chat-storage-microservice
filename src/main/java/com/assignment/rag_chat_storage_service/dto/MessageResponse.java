package com.assignment.rag_chat_storage_service.dto;

import com.assignment.rag_chat_storage_service.constant.SenderType;
import lombok.Builder;

import java.time.LocalDateTime;

public record MessageResponse(Long messageId, SenderType senderType, String content, LocalDateTime createdAt) {
    @Builder
    public MessageResponse{}
}
