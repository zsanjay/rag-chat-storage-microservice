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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/sessions")
@Tag(name = "Messages", description = "Manage chat messages")
@Slf4j
public class MessageController {

    private MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping(value = "/{sessionId}/messages")
    @Operation(summary = "Add a message to a session")
    public ResponseEntity<MessageResponseDto> addMessage(@PathVariable Long sessionId,
                                                         @RequestHeader(value = "X-User-Id") String userId,
                                                         @RequestBody MessageRequestDto message) throws Exception {
        log.info("Adding new message for sessionId={} | userId={} | senderType={} | content={}",
                sessionId, userId, message.senderType(), message.content());
        try {
            MessageResponseDto response = this.messageService.addMessage(sessionId, userId, message);
            log.info("Message successfully added to sessionId={} | messageId={}",
                    sessionId, response.message().messageId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (SessionNotFoundException e) {
            log.error("Session not found while adding message | sessionId={}", sessionId, e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while adding message | sessionId={} | userId={} | error={}",
                    sessionId, userId, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping(value = "/{sessionId}/messages")
    @Operation(summary = "Get all messages for a session")
    public ResponseEntity<PagedResult<MessagesResponseDto>> getMessageHistory(@PathVariable Long sessionId,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "50") int size) throws SessionNotFoundException, MessageNotFoundException {
        log.info("Fetching message history | sessionId={} | page={} | size={}", sessionId, page, size);

        try {
            PagedResult<MessagesResponseDto> result = this.messageService.getMessageHistory(sessionId, page, size);
            log.info("Fetched {} messages for sessionId={} (page={}, size={})",
                    result.getSize(), sessionId, page, size);
            return ResponseEntity.ok(result);
        } catch (SessionNotFoundException | MessageNotFoundException e) {
            log.error("Error fetching message history | sessionId={} | error={}", sessionId, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error fetching message history | sessionId={} | error={}", sessionId, e.getMessage(), e);
            throw e;
        }
    }
}
