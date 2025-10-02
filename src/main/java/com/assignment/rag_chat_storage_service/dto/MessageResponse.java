package com.assignment.rag_chat_storage_service.dto;

import java.util.List;

public record MessageResponse(Long sessionId, List<MessageRequest> messages) {
}
