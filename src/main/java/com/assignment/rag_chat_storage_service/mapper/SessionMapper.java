package com.assignment.rag_chat_storage_service.mapper;

import com.assignment.rag_chat_storage_service.dto.SessionRequestDto;
import com.assignment.rag_chat_storage_service.dto.SessionResponseDto;
import com.assignment.rag_chat_storage_service.model.Session;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SessionMapper {
    Session dtoToSession(SessionRequestDto sessionRequest);
    SessionResponseDto sessionToDto(Session session);
}
