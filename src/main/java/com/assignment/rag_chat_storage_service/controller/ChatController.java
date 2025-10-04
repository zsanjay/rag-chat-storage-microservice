package com.assignment.rag_chat_storage_service.controller;

import com.assignment.rag_chat_storage_service.dto.ChatDto;
import com.assignment.rag_chat_storage_service.exception.SessionNotFoundException;
import com.assignment.rag_chat_storage_service.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sessions")
@Tag(name = "Chat", description = "Chat with the AI Assistant")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping(value = "/{sessionId}/chat")
    @Operation(summary = "Send message and get response from AI Assistant")
    public ResponseEntity<ChatDto> sendMessage(@PathVariable Long sessionId,
                                               @RequestBody ChatDto chatDto) throws SessionNotFoundException {
        return ResponseEntity.ok(chatService.handleMessage(sessionId, chatDto));
    }
}
