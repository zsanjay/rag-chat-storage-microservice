package com.assignment.rag_chat_storage_service.service.impl;

import com.assignment.rag_chat_storage_service.dto.MessageRequest;
import com.assignment.rag_chat_storage_service.dto.MessageResponse;
import com.assignment.rag_chat_storage_service.mapper.MessageMapper;
import com.assignment.rag_chat_storage_service.model.Session;
import com.assignment.rag_chat_storage_service.repository.SessionRepository;
import com.assignment.rag_chat_storage_service.service.MessageService;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    private final SessionRepository sessionRepository;
    private final MessageMapper messageMapper;

    public MessageServiceImpl(SessionRepository sessionRepository, MessageMapper messageMapper) {
        this.sessionRepository = sessionRepository;
        this.messageMapper = messageMapper;
    }

    @Override
    public MessageResponse addMessage(Long sessionId, MessageRequest message) {
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException(String.format("Session doesn't  exists for the id: %s", sessionId)));
        session.getMessages().add(messageMapper.dtoToMessage(message));
        session = sessionRepository.save(session);
        return messageMapper.sessionToDto(session);
    }
}
