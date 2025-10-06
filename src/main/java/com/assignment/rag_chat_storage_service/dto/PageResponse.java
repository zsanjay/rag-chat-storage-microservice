package com.assignment.rag_chat_storage_service.dto;

import lombok.Builder;

@Builder
public record PageResponse<T>(T data, int page, int size, long totalElements, int totalPages) {}
