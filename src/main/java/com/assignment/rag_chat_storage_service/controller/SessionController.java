package com.assignment.rag_chat_storage_service.controller;

import com.assignment.rag_chat_storage_service.dto.*;
import com.assignment.rag_chat_storage_service.exception.SessionAlreadyExistsException;
import com.assignment.rag_chat_storage_service.exception.SessionNotFoundException;
import com.assignment.rag_chat_storage_service.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/sessions")
@Tag(name = "Sessions", description = "Manage chat sessions")
public class SessionController {

    private SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new chat session")
    public ResponseEntity<SessionResponseDto> createSession(@RequestBody SessionRequestDto session) throws SessionAlreadyExistsException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.sessionService.createSession(session));
    }

    @GetMapping
    @Operation(summary = "Get all chat sessions")
    public ResponseEntity<PagedResult<List<SessionResponseDto>>> getSessions(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(this.sessionService.getSessions(page, size), HttpStatus.OK);
    }

    @GetMapping(value = "/{sessionId}")
    @Operation(summary = "Get session by sessionId")
    public ResponseEntity<SessionResponseDto> getSession(@PathVariable Long sessionId) throws SessionNotFoundException {
        return new ResponseEntity<>(this.sessionService.getSession(sessionId) , HttpStatus.OK);
    }

    @PutMapping(value = "/{sessionId}")
    @Operation(summary = "Rename chat session")
    public ResponseEntity<SessionResponseDto> updateSessionTitle(@PathVariable Long sessionId,
                                                                 @RequestBody TitleChangeRequestDto titleChangeRequest) throws SessionNotFoundException {
        return new ResponseEntity<>(this.sessionService.updateSessionTitle(sessionId , titleChangeRequest), HttpStatus.OK);
    }

    @PatchMapping(value = "/{sessionId}/favorite")
    @Operation(summary = "Marked or unmarked chat session as favorite")
    public ResponseEntity<SessionResponseDto> updateFavorite(@PathVariable Long sessionId,
                                                             @RequestBody FavoriteRequestDto favoriteRequest) throws SessionNotFoundException {
        return new ResponseEntity<>(this.sessionService.updateFavorite(sessionId, favoriteRequest), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{sessionId}")
    @Operation(summary = "Delete chat session and all the messages")
    public ResponseEntity<Void> deleteSession(@PathVariable Long sessionId) throws SessionNotFoundException {
        this.sessionService.deleteSession(sessionId);
        return ResponseEntity.noContent().build();
    }
}
