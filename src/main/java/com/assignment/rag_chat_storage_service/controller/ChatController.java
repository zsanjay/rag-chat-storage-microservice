package com.assignment.rag_chat_storage_service.controller;

import com.assignment.rag_chat_storage_service.dto.ChatDto;
import com.assignment.rag_chat_storage_service.exception.SessionNotFoundException;
import com.assignment.rag_chat_storage_service.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sessions")
@Tag(name = "Chat", description = "Chat with the AI Assistant")
@Slf4j
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping(value = "/{sessionId}/chat")
    @Operation(summary = "Send message and get response from AI Assistant")
    public ResponseEntity<ChatDto> sendMessage(@PathVariable Long sessionId,
                                               @RequestBody ChatDto chatDto) throws SessionNotFoundException {
        log.info("Received chat request for sessionId={} | userMessage={}", sessionId, chatDto.message());

        try {
            ChatDto response = chatService.handleMessage(sessionId, chatDto);
            log.info("Successfully processed chat for sessionId={} | aiResponse={}", sessionId, response.message());
            return ResponseEntity.ok(response);
        } catch (SessionNotFoundException e) {
            log.error("Session not found for sessionId={}", sessionId, e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while processing chat for sessionId={}: {}", sessionId, e.getMessage(), e);
            throw e;
        }
    }
}
