package com.assignment.rag_chat_storage_service.controller;

import com.assignment.rag_chat_storage_service.dto.*;
import com.assignment.rag_chat_storage_service.exception.NoSessionFoundException;
import com.assignment.rag_chat_storage_service.exception.SessionAlreadyExistsException;
import com.assignment.rag_chat_storage_service.exception.SessionNotFoundException;
import com.assignment.rag_chat_storage_service.model.Session;
import com.assignment.rag_chat_storage_service.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/sessions")
@Tag(name = "Sessions", description = "Manage chat sessions")
@Slf4j
public class SessionController {

    private SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new chat session")
    public ResponseEntity<SessionResponseDto> createSession(@RequestBody SessionRequestDto session) throws SessionAlreadyExistsException {
        log.info("Creating session for userId={} | title={}", session.userId(), session.title());
        try {
            SessionResponseDto sessionResponseDto = this.sessionService.createSession(session);
            log.info("Session successfully created with sessionId={} | title={}",
                    sessionResponseDto.sessionId(), session.title());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(sessionResponseDto);
        } catch (SessionAlreadyExistsException e) {
            log.error("Session already exists for title={}", session.title(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while creating session | title={} | error={}",
                    session.title(), e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping
    @Operation(summary = "Get all chat sessions")
    public ResponseEntity<PageResponse<List<SessionResponseDto>>> getSessions(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        log.info("Getting all sessions of page={} and size={}", page, size);
        try {
            PageResponse<List<SessionResponseDto>> sessionResponseDtoList = this.sessionService.getSessions(page, size);
            log.info("Sessions successfully retrieved with size={}", sessionResponseDtoList.size());
            return new ResponseEntity<>(sessionResponseDtoList, HttpStatus.OK);
        } catch (NoSessionFoundException e) {
            log.error("No Session found", e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while getting sessions error={}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping(value = "/{sessionId}")
    @Operation(summary = "Get session by sessionId")
    public ResponseEntity<SessionResponseDto> getSession(@PathVariable Long sessionId) throws SessionNotFoundException {
        log.info("Get session by sessionId={}", sessionId);
        try {
            SessionResponseDto sessionResponseDto = this.sessionService.getSession(sessionId);
            log.info("Sessions successfully retrieved with sessionId={}", sessionId);
            return new ResponseEntity<>(sessionResponseDto , HttpStatus.OK);
        } catch (SessionNotFoundException e) {
            log.error("Session not found for sessionId={}", sessionId, e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while getting session with sessionId={} | error={}", sessionId, e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping(value = "/{sessionId}")
    @Operation(summary = "Rename chat session")
    public ResponseEntity<SessionResponseDto> updateSessionTitle(@PathVariable Long sessionId,
                                                                 @RequestBody TitleChangeRequestDto titleChangeRequest) throws SessionNotFoundException {
        log.info("Renaming session with title={}", titleChangeRequest.title());
        try {
            SessionResponseDto sessionResponseDto = this.sessionService.updateSessionTitle(sessionId , titleChangeRequest);
            log.info("Session successfully renamed with sessionId={}", sessionId);
            return new ResponseEntity<>(sessionResponseDto , HttpStatus.OK);
        } catch (SessionNotFoundException e) {
            log.error("Session not found for sessionId={}", sessionId, e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while renaming session with sessionId={} | error={}", sessionId, e.getMessage(), e);
            throw e;
        }
    }

    @PatchMapping(value = "/{sessionId}/favorite")
    @Operation(summary = "Marked or unmarked chat session as favorite")
    public ResponseEntity<SessionResponseDto> updateFavorite(@PathVariable Long sessionId,
                                                             @RequestBody FavoriteRequestDto favoriteRequest) throws SessionNotFoundException {
        log.info("Updating favorite to {} for sessionId={}",favoriteRequest.isFavorite(), sessionId);
        try {
            SessionResponseDto sessionResponseDto = this.sessionService.updateFavorite(sessionId, favoriteRequest);
            log.info("Session successfully updated with sessionId={}", sessionId);
            return new ResponseEntity<>(sessionResponseDto , HttpStatus.OK);
        } catch (SessionNotFoundException e) {
            log.error("Session not found for sessionId={}", sessionId, e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while updated session favorite with sessionId={} | error={}", sessionId, e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping(value = "/{sessionId}")
    @Operation(summary = "Delete chat session and all the messages")
    public ResponseEntity<Void> deleteSession(@PathVariable Long sessionId) throws SessionNotFoundException {
        log.info("Deleting session with sessionId={}", sessionId);
        try {
            this.sessionService.deleteSession(sessionId);
            log.info("Sessions successfully deleted with sessionId={}", sessionId);
            return ResponseEntity.noContent().build();
        } catch (SessionNotFoundException e) {
            log.error("Session not found for sessionId={}", sessionId, e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while deleting session with sessionId={} | error={}", sessionId, e.getMessage(), e);
            throw e;
        }
    }
}
