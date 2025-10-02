package com.assignment.rag_chat_storage_service.dto;

import com.assignment.rag_chat_storage_service.model.Message;
import java.util.List;

public record SessionResponse(Long sessionId, String title, boolean isFavorite, List<Message> messages) {
}
