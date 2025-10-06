package com.assignment.rag_chat_storage_service.service.impl;

import com.assignment.rag_chat_storage_service.constant.Constants;
import com.assignment.rag_chat_storage_service.dto.*;
import com.assignment.rag_chat_storage_service.exception.MessageNotFoundException;
import com.assignment.rag_chat_storage_service.exception.SessionNotFoundException;
import com.assignment.rag_chat_storage_service.exception.UnauthorizedAccessException;
import com.assignment.rag_chat_storage_service.mapper.MessageMapper;
import com.assignment.rag_chat_storage_service.model.Message;
import com.assignment.rag_chat_storage_service.model.Session;
import com.assignment.rag_chat_storage_service.repository.MessageRepository;
import com.assignment.rag_chat_storage_service.repository.SessionRepository;
import com.assignment.rag_chat_storage_service.service.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final SessionRepository sessionRepository;
    private final MessageMapper messageMapper;
    private final MessageRepository messageRepository;

    public MessageServiceImpl(SessionRepository sessionRepository, MessageMapper messageMapper, MessageRepository messageRepository) {
        this.sessionRepository = sessionRepository;
        this.messageMapper = messageMapper;
        this.messageRepository = messageRepository;
    }

    @Transactional
    @Override
    public MessageResponseDto addMessage(Long sessionId, String userId, MessageRequestDto messageRequest) {
        Session session = getSessionBySessionId(sessionId);
        if(!session.getUser().equals(userId)) {
            throw new UnauthorizedAccessException(userId + " is not allowed to add this message to the session : "+ sessionId);
        }
        Message message = messageMapper.dtoToMessage(messageRequest);
        message.setSession(session);
        session.getMessages().add(message);
        Message savedMessage = messageRepository.save(message);
        return messageMapper.messageToDto(sessionId, userId, savedMessage);
    }

    @Transactional
    @Override
    public PageResponse<MessagesResponseDto> getMessageHistory(Long sessionId, int page, int size) throws SessionNotFoundException , MessageNotFoundException {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Constants.CREATED_AT).ascending());
        Session session = getSessionBySessionId(sessionId);
        Page<Message> messages = messageRepository.findAllBySession(session, pageable);
        if(messages.getSize() == 0) {
            throw new MessageNotFoundException(sessionId);
        }
        return new PageResponse<>(messageMapper.messagesToDto(sessionId, session.getUser(), messages.toList()), page, size, messages.getTotalElements(), messages.getTotalPages());
    }

    private Session getSessionBySessionId(Long sessionId) throws SessionNotFoundException {
        return sessionRepository.findById(sessionId).orElseThrow(() -> new SessionNotFoundException(sessionId));
    }
}
