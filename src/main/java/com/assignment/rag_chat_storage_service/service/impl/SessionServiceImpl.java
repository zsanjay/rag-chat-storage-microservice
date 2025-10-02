package com.assignment.rag_chat_storage_service.service.impl;

import com.assignment.rag_chat_storage_service.dto.*;
import com.assignment.rag_chat_storage_service.mapper.SessionMapper;
import com.assignment.rag_chat_storage_service.model.Session;
import com.assignment.rag_chat_storage_service.repository.SessionRepository;
import com.assignment.rag_chat_storage_service.service.SessionService;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
public class SessionServiceImpl implements SessionService {

    private SessionRepository sessionRepository;
    private SessionMapper sessionMapper;

    public SessionServiceImpl(SessionRepository sessionRepository, SessionMapper sessionMapper) {
        this.sessionRepository = sessionRepository;
        this.sessionMapper = sessionMapper;
    }
    @Override
    public SessionResponse createSession(SessionRequest sessionRequest) {
       Session session =  sessionRepository.findByTitle(sessionRequest.title());
       if(Objects.nonNull(session)){
           throw new RuntimeException(String.format("Session already exists for the title: %s", sessionRequest.title()));
       }
       session = sessionMapper.dtoToSession(sessionRequest);
       session = sessionRepository.save(session);
       return sessionMapper.sessionToDto(session);
    }

    @Override
    public SessionResponse updateSessionTitle(Long sessionId, TitleChangeRequest titleChangeRequest) {
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException(String.format("Session doesn't  exists for the id: %s", sessionId)));
        session.setTitle(titleChangeRequest.title());
        sessionRepository.save(session);
        return sessionMapper.sessionToDto(session);
    }

    @Override
    public SessionResponse updateFavorite(Long sessionId, FavoriteRequest favoriteRequest) {
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException(String.format("Session doesn't  exists for the id: %s", sessionId)));
        session.setFavorite(favoriteRequest.isFavorite());
        sessionRepository.save(session);
        return sessionMapper.sessionToDto(session);
    }

    @Override
    public void deleteSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException(String.format("Session doesn't  exists for the id: %s", sessionId)));
        sessionRepository.delete(session);
    }
}
