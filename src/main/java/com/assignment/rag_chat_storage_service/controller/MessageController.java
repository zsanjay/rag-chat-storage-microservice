package com.assignment.rag_chat_storage_service.controller;

import com.assignment.rag_chat_storage_service.dto.MessageRequest;
import com.assignment.rag_chat_storage_service.dto.MessageResponse;
import com.assignment.rag_chat_storage_service.dto.SessionResponse;
import com.assignment.rag_chat_storage_service.service.MessageService;
import com.assignment.rag_chat_storage_service.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/sessions")
public class MessageController {

    private MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping(value = "/{sessionId}/messages")
    public ResponseEntity<MessageResponse> addMessage(@PathVariable Long sessionId, @RequestBody MessageRequest message) {
        return new ResponseEntity<>(this.messageService.addMessage(sessionId, message), HttpStatus.OK);
    }
}
