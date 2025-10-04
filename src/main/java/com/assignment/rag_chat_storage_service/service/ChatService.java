package com.assignment.rag_chat_storage_service.service;

import com.assignment.rag_chat_storage_service.dto.ChatDto;
import com.assignment.rag_chat_storage_service.exception.SessionNotFoundException;

public interface ChatService {
    ChatDto handleMessage(Long sessionId,ChatDto chatRequest) throws SessionNotFoundException;
}
