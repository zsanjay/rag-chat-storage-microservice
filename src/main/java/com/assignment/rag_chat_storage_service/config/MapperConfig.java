package com.assignment.rag_chat_storage_service.config;

import com.assignment.rag_chat_storage_service.dto.*;
import com.assignment.rag_chat_storage_service.mapper.MessageMapper;
import com.assignment.rag_chat_storage_service.mapper.SessionMapper;
import com.assignment.rag_chat_storage_service.model.Message;
import com.assignment.rag_chat_storage_service.model.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Configuration
public class MapperConfig {

    @Bean
    public SessionMapper sessionMapper() {
        return new SessionMapper() {
            @Override
            public Session dtoToSession(SessionRequestDto sessionRequest) {
                return Session.builder()
                        .user(sessionRequest.userId())
                        .title(sessionRequest.title())
                        .isFavorite(sessionRequest.isFavorite())
                        .build();
            }

            @Override
            public SessionResponseDto sessionToDto(Session session) {
                List<MessageRequestDto> messages = new ArrayList<>();
                if(Objects.nonNull(session.getMessages())) {
                    session.getMessages().forEach(message -> {
                        messages.add(MessageRequestDto.builder()
                                .senderType(message.getSenderType())
                                .content(message.getContent())
                                .build());
                    });
                }
                return SessionResponseDto.builder()
                        .userId(session.getUser())
                        .sessionId(session.getId())
                        .title(session.getTitle())
                        .isFavorite(session.isFavorite())
                        .messages(messages)
                        .build();
            }
        };
    }

    @Bean
    public MessageMapper messageMapper() {
        return new MessageMapper() {
            @Override
            public Message dtoToMessage(MessageRequestDto messageRequest) {
                return Message.builder()
                        .senderType(messageRequest.senderType())
                        .content(messageRequest.content())
                        .build();
            }

            @Override
            public MessageResponseDto messageToDto(Long sessionId, String userId, Message message) {
                return MessageResponseDto.builder()
                        .sessionId(sessionId)
                        .userId(userId)
                        .message(MessageResponse.builder()
                                .messageId(message.getId())
                                .createdAt(message.getCreatedAt())
                                .senderType(message.getSenderType())
                                .content(message.getContent())
                                .build())
                        .build();
            }

            @Override
            public MessagesResponseDto messagesToDto(Long sessionId, String userId, List<Message> messages) {
                return MessagesResponseDto.builder()
                        .sessionId(sessionId)
                        .userId(userId)
                        .messages(messages.stream()
                                .map(message -> MessageResponse.builder()
                                        .messageId(message.getId())
                                        .content(message.getContent())
                                        .createdAt(message.getCreatedAt())
                                        .senderType(message.getSenderType())
                                        .build()
                                ).toList())
                        .build();

            }
        };
    }
}
