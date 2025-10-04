package com.assignment.rag_chat_storage_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedResult<T> {
    private T content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
