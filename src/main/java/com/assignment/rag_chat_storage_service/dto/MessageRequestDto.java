package com.assignment.rag_chat_storage_service.dto;

import com.assignment.rag_chat_storage_service.constant.SenderType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public record MessageRequestDto(@Schema(description = "Sender type: USER or ASSISTANT") SenderType senderType,
                                @Schema(description = "Message content") String content) {

    @Builder
    public MessageRequestDto {}
}
