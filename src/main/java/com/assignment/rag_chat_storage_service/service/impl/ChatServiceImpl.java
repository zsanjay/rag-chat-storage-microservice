package com.assignment.rag_chat_storage_service.service.impl;

import com.assignment.rag_chat_storage_service.constant.SenderType;
import com.assignment.rag_chat_storage_service.dto.ChatDto;
import com.assignment.rag_chat_storage_service.dto.MessageRequestDto;
import com.assignment.rag_chat_storage_service.exception.SessionNotFoundException;
import com.assignment.rag_chat_storage_service.mapper.MessageMapper;
import com.assignment.rag_chat_storage_service.model.Message;
import com.assignment.rag_chat_storage_service.model.Session;
import com.assignment.rag_chat_storage_service.repository.MessageRepository;
import com.assignment.rag_chat_storage_service.repository.SessionRepository;
import com.assignment.rag_chat_storage_service.service.ChatService;
import com.assignment.rag_chat_storage_service.service.OpenAIService;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

    private final SessionRepository sessionRepository;
    private final OpenAIService openAIService;
    private final MessageMapper messageMapper;
    private final MessageRepository messageRepository;

    public ChatServiceImpl(SessionRepository sessionRepository,
                           OpenAIService openAIService, MessageMapper messageMapper,
                           MessageRepository messageRepository) {
        this.sessionRepository = sessionRepository;
        this.openAIService = openAIService;
        this.messageMapper = messageMapper;
        this.messageRepository = messageRepository;
    }

    @Override
    public ChatDto handleMessage(Long sessionId, ChatDto chatRequest) throws SessionNotFoundException {
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new SessionNotFoundException(sessionId));
        addMessage(session, chatRequest.message(), SenderType.USER);
        String response = openAIService.generateResponse(chatRequest.message());
        addMessage(session, response, SenderType.ASSISTANT);
        sessionRepository.save(session);
        return new ChatDto(response);
    }

    private void addMessage(Session session, String content, SenderType senderType) {
        Message message = messageMapper.dtoToMessage(MessageRequestDto.builder().senderType(senderType).content(content).build());
        message.setSession(session);
        session.getMessages().add(messageRepository.save(message));
    }
}
