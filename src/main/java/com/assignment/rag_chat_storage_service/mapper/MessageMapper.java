package com.assignment.rag_chat_storage_service.mapper;

import com.assignment.rag_chat_storage_service.dto.MessageRequestDto;
import com.assignment.rag_chat_storage_service.dto.MessageResponse;
import com.assignment.rag_chat_storage_service.dto.MessageResponseDto;
import com.assignment.rag_chat_storage_service.dto.MessagesResponseDto;
import com.assignment.rag_chat_storage_service.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    Message dtoToMessage(MessageRequestDto messageRequest);

    @Mapping(target = "messageId", source = "id")
    MessageResponse messageToMessageResponse(Message message);

    default MessageResponseDto messageToDto(Long sessionId, String userId, Message message) {
        return MessageResponseDto.builder()
                .sessionId(sessionId)
                .userId(userId)
                .message(messageToMessageResponse(message))
                .build();
    }

    List<MessageResponse> messagesToResponses(List<Message> messages);

    default MessagesResponseDto messagesToDto(Long sessionId, String userId, List<Message> messages) {
        return MessagesResponseDto.builder()
                .sessionId(sessionId)
                .userId(userId)
                .messages(messagesToResponses(messages))
                .build();
    }
}
