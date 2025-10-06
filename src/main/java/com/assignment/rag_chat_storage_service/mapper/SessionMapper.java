package com.assignment.rag_chat_storage_service.mapper;

import com.assignment.rag_chat_storage_service.dto.SessionRequestDto;
import com.assignment.rag_chat_storage_service.dto.SessionResponseDto;
import com.assignment.rag_chat_storage_service.model.Session;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MessageMapper.class})
public interface SessionMapper {
    @Mapping(target = "user", source = "userId")
    Session dtoToSession(SessionRequestDto sessionRequest);

    @Mapping(target = "userId", source = "user")
    @Mapping(target = "sessionId", source = "id")
    @Mapping(target = "messages", source = "messages")
    @Mapping(target = "isFavorite", source = "favorite")
    SessionResponseDto sessionToDto(Session session);
}
