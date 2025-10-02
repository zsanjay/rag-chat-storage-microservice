package com.assignment.rag_chat_storage_service.mapper;

import com.assignment.rag_chat_storage_service.dto.MessageRequest;
import com.assignment.rag_chat_storage_service.dto.MessageResponse;
import com.assignment.rag_chat_storage_service.model.Message;
import com.assignment.rag_chat_storage_service.model.Session;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    Message dtoToMessage(MessageRequest messageRequest);
    MessageResponse sessionToDto(Session session);
}
