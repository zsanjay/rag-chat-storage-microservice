package com.assignment.rag_chat_storage_service.service;

import com.assignment.rag_chat_storage_service.dto.MessageRequest;
import com.assignment.rag_chat_storage_service.dto.MessageResponse;

public interface MessageService {

    MessageResponse addMessage(Long sessionId, MessageRequest message);
}
