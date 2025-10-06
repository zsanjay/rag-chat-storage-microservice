package com.assignment.rag_chat_storage_service.service;

import com.assignment.rag_chat_storage_service.dto.*;
import com.assignment.rag_chat_storage_service.exception.SessionAlreadyExistsException;
import com.assignment.rag_chat_storage_service.exception.SessionNotFoundException;
import com.assignment.rag_chat_storage_service.model.Session;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SessionService {

    SessionResponseDto createSession(SessionRequestDto session) throws SessionAlreadyExistsException;

    PageResponse<List<SessionResponseDto>> getSessions(int page, int size);

    SessionResponseDto updateSessionTitle(Long sessionId, TitleChangeRequestDto titleChangeRequest) throws SessionNotFoundException;

    SessionResponseDto updateFavorite(Long sessionId, FavoriteRequestDto favoriteRequest) throws SessionNotFoundException;

    void deleteSession(Long sessionId) throws SessionNotFoundException;

    SessionResponseDto getSession(Long sessionId) throws SessionNotFoundException;
}
