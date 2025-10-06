package com.assignment.rag_chat_storage_service.dto;

import lombok.Builder;
import java.io.Serializable;
import java.util.List;

@Builder
public record SessionResponseDto(Long sessionId, String userId,String title, boolean isFavorite, List<MessageRequestDto> messages) implements Serializable {

}
