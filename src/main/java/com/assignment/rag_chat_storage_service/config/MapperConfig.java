package com.assignment.rag_chat_storage_service.config;

import com.assignment.rag_chat_storage_service.dto.MessageRequest;
import com.assignment.rag_chat_storage_service.dto.MessageResponse;
import com.assignment.rag_chat_storage_service.dto.SessionRequest;
import com.assignment.rag_chat_storage_service.dto.SessionResponse;
import com.assignment.rag_chat_storage_service.mapper.MessageMapper;
import com.assignment.rag_chat_storage_service.mapper.SessionMapper;
import com.assignment.rag_chat_storage_service.model.Message;
import com.assignment.rag_chat_storage_service.model.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public SessionMapper sessionMapper() {
        return new SessionMapper() {
            @Override
            public Session dtoToSession(SessionRequest sessionRequest) {
                return new Session(sessionRequest.title(), sessionRequest.isFavorite());
            }

            @Override
            public SessionResponse sessionToDto(Session session) {
                return new SessionResponse(session.getId(), session.getTitle(), session.isFavorite(), session.getMessages());
            }
        };
    }

    @Bean
    public MessageMapper messageMapper() {
        return new MessageMapper() {
            @Override
            public Message dtoToMessage(MessageRequest messageRequest) {
                return new Message(messageRequest.content(), messageRequest.senderType());
            }

            @Override
            public MessageResponse sessionToDto(Session session) {
                return new MessageResponse(session.getId(),
                        session.getMessages().stream()
                                .map(message -> new MessageRequest(message.getSenderType(), message.getContent())).toList());
            }
        };
    }
}
