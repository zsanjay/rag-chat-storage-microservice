package com.assignment.rag_chat_storage_service.service;

import com.assignment.rag_chat_storage_service.dto.*;

public interface SessionService {

    SessionResponse createSession(SessionRequest session);

    SessionResponse updateSessionTitle(Long sessionId, TitleChangeRequest titleChangeRequest);

    SessionResponse updateFavorite(Long sessionId, FavoriteRequest favoriteRequest);

    void deleteSession(Long sessionId);
}
