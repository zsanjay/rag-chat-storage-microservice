package com.assignment.rag_chat_storage_service.service;

import com.assignment.rag_chat_storage_service.dto.MessageRequestDto;
import com.assignment.rag_chat_storage_service.dto.MessageResponseDto;
import com.assignment.rag_chat_storage_service.dto.MessagesResponseDto;
import com.assignment.rag_chat_storage_service.dto.PagedResult;
import com.assignment.rag_chat_storage_service.exception.MessageNotFoundException;
import com.assignment.rag_chat_storage_service.exception.SessionNotFoundException;

public interface MessageService {

    MessageResponseDto addMessage(Long sessionId, String userId, MessageRequestDto message) throws Exception;
    PagedResult<MessagesResponseDto> getMessageHistory(Long sessionId, int page, int size) throws SessionNotFoundException, MessageNotFoundException;
}
