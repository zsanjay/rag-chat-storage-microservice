package com.assignment.rag_chat_storage_service.service.impl;

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
        //MessageResponse.builder().build().
        return messageMapper.messageToDto(sessionId, userId, savedMessage);
    }

    @Transactional
    @Override
    public PagedResult<MessagesResponseDto> getMessageHistory(Long sessionId, int page, int size) throws SessionNotFoundException , MessageNotFoundException {
        Session session = getSessionBySessionId(sessionId);
        long totalElements = session.getMessages().size();
        if(totalElements == 0) {
            throw new MessageNotFoundException(sessionId);
        }
        int totalPages = (int) Math.ceil((double) totalElements / size);
        List<Message> messageList = session.getMessages().stream().skip((long) page * size).limit(size).toList();
        return new PagedResult<>(messageMapper.messagesToDto(sessionId, session.getUser(), messageList), page, size, totalElements, totalPages);
    }

    private Session getSessionBySessionId(Long sessionId) throws SessionNotFoundException {
        return sessionRepository.findById(sessionId).orElseThrow(() -> new SessionNotFoundException(sessionId));
    }
}
