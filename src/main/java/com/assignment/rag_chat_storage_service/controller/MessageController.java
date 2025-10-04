package com.assignment.rag_chat_storage_service.controller;

import com.assignment.rag_chat_storage_service.dto.MessageRequestDto;
import com.assignment.rag_chat_storage_service.dto.MessageResponseDto;
import com.assignment.rag_chat_storage_service.dto.MessagesResponseDto;
import com.assignment.rag_chat_storage_service.dto.PagedResult;
import com.assignment.rag_chat_storage_service.exception.MessageNotFoundException;
import com.assignment.rag_chat_storage_service.exception.SessionNotFoundException;
import com.assignment.rag_chat_storage_service.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/sessions")
@Tag(name = "Messages", description = "Manage chat messages")
public class MessageController {

    private MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping(value = "/{sessionId}/messages")
    @Operation(summary = "Add a message to a session")
    public ResponseEntity<MessageResponseDto> addMessage(@PathVariable Long sessionId,
                                                         @RequestHeader(value = "X-User-Id", required = true) String userId,
                                                         @RequestBody MessageRequestDto message) throws Exception {
        return new ResponseEntity<>(this.messageService.addMessage(sessionId, userId, message), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{sessionId}/messages")
    @Operation(summary = "Get all messages for a session")
    public ResponseEntity<PagedResult<MessagesResponseDto>> getMessageHistory(@PathVariable Long sessionId,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "50") int size) throws SessionNotFoundException, MessageNotFoundException {
        return ResponseEntity.ok(this.messageService.getMessageHistory(sessionId, page, size));
    }
}
