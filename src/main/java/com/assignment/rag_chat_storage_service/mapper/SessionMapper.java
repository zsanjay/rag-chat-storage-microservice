package com.assignment.rag_chat_storage_service.mapper;

import com.assignment.rag_chat_storage_service.dto.SessionRequest;
import com.assignment.rag_chat_storage_service.dto.SessionResponse;
import com.assignment.rag_chat_storage_service.model.Session;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SessionMapper {
    Session dtoToSession(SessionRequest sessionRequest);
    SessionResponse sessionToDto(Session session);
}
