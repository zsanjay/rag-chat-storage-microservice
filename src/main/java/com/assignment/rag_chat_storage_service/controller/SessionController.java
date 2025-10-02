package com.assignment.rag_chat_storage_service.controller;

import com.assignment.rag_chat_storage_service.dto.*;
import com.assignment.rag_chat_storage_service.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/sessions")
public class SessionController {

    private SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public ResponseEntity<SessionResponse> createSession(@RequestBody SessionRequest session) {
        return new ResponseEntity<>(this.sessionService.createSession(session) , HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{sessionId}/title")
    public ResponseEntity<SessionResponse> updateSessionTitle(@PathVariable Long sessionId, @RequestBody TitleChangeRequest titleChangeRequest) {
        return new ResponseEntity<>(this.sessionService.updateSessionTitle(sessionId , titleChangeRequest), HttpStatus.OK);
    }

    @PatchMapping(value = "/{sessionId}/favorite")
    public ResponseEntity<SessionResponse> updateFavorite(@PathVariable Long sessionId, @RequestBody FavoriteRequest favoriteRequest) {
        return new ResponseEntity<>(this.sessionService.updateFavorite(sessionId, favoriteRequest), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{sessionId}")
    public void deleteSession(@PathVariable Long sessionId) {
        this.sessionService.deleteSession(sessionId);
    }
}
