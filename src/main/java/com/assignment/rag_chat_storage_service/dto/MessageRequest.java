package com.assignment.rag_chat_storage_service.dto;

import com.assignment.rag_chat_storage_service.constant.SenderType;

public record MessageRequest(SenderType senderType, String content) {
}
