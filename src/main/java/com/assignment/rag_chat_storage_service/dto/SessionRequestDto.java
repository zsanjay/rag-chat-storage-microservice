package com.assignment.rag_chat_storage_service.dto;

import lombok.Builder;

import java.io.Serializable;

public record SessionRequestDto(String userId, String title, boolean isFavorite) implements Serializable {

    @Builder
    public SessionRequestDto {}
}
