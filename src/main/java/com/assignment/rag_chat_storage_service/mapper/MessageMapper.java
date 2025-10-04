package com.assignment.rag_chat_storage_service.mapper;

import com.assignment.rag_chat_storage_service.dto.MessageRequestDto;
import com.assignment.rag_chat_storage_service.dto.MessageResponseDto;
import com.assignment.rag_chat_storage_service.dto.MessagesResponseDto;
import com.assignment.rag_chat_storage_service.model.Message;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    Message dtoToMessage(MessageRequestDto messageRequest);
    MessageResponseDto messageToDto(Long sessionId, String userId, Message message);
    MessagesResponseDto messagesToDto(Long sessionId , String userId, List<Message> messages);
}
