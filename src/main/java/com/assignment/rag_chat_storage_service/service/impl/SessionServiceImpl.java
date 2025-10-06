package com.assignment.rag_chat_storage_service.service.impl;

import com.assignment.rag_chat_storage_service.constant.Constants;
import com.assignment.rag_chat_storage_service.dto.*;
import com.assignment.rag_chat_storage_service.exception.NoSessionFoundException;
import com.assignment.rag_chat_storage_service.exception.SessionAlreadyExistsException;
import com.assignment.rag_chat_storage_service.exception.SessionNotFoundException;
import com.assignment.rag_chat_storage_service.mapper.SessionMapper;
import com.assignment.rag_chat_storage_service.model.Session;
import com.assignment.rag_chat_storage_service.repository.SessionRepository;
import com.assignment.rag_chat_storage_service.service.SessionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SessionServiceImpl implements SessionService {

    private SessionRepository sessionRepository;
    private SessionMapper sessionMapper;

    public SessionServiceImpl(SessionRepository sessionRepository, SessionMapper sessionMapper) {
        this.sessionRepository = sessionRepository;
        this.sessionMapper = sessionMapper;
    }
    @Override
    public SessionResponseDto createSession(SessionRequestDto sessionRequest) {
       Session session =  sessionRepository.findByTitle(sessionRequest.title());
       if(Objects.nonNull(session)){
           throw new SessionAlreadyExistsException(sessionRequest.title());
       }
       session = sessionMapper.dtoToSession(sessionRequest);
       session = sessionRepository.save(session);
       return sessionMapper.sessionToDto(session);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<List<SessionResponseDto>> getSessions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Constants.CREATED_AT).ascending());
        Page<Session> sessionPage = sessionRepository.findAllWithoutMessages(pageable);
        if(sessionPage.getTotalElements() == 0) {
            throw new NoSessionFoundException();
        }
        List<SessionResponseDto> sessionResponseDtoList =  sessionPage.stream()
                .map(session -> sessionMapper.sessionToDto(session)).collect(Collectors.toList());
        return new PageResponse<>(sessionResponseDtoList, page, size, sessionPage.getTotalElements(), sessionPage.getTotalPages());
    }

    @Transactional
    @Override
    public SessionResponseDto updateSessionTitle(Long sessionId, TitleChangeRequestDto titleChangeRequest) throws SessionNotFoundException {
        Session session = sessionRepository.findByIdWithoutMessages(sessionId).orElseThrow(() -> new SessionNotFoundException(sessionId));
        session.setTitle(titleChangeRequest.title());
        session = sessionRepository.save(session);
        return sessionMapper.sessionToDto(session);
    }

    @Transactional
    @Override
    public SessionResponseDto updateFavorite(Long sessionId, FavoriteRequestDto favoriteRequest) {
        Session session = sessionRepository.findByIdWithoutMessages(sessionId).orElseThrow(() -> new SessionNotFoundException(sessionId));
        session.setFavorite(favoriteRequest.isFavorite());
        sessionRepository.save(session);
        return sessionMapper.sessionToDto(session);
    }

    @Override
    public void deleteSession(Long sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    @Transactional(readOnly = true)
    @Override
    public SessionResponseDto getSession(Long sessionId) throws SessionNotFoundException {
        Session session = getSessionByIdWithMessages(sessionId);
        return sessionMapper.sessionToDto(session);
    }

    private Session getSessionBySessionId(Long sessionId) throws SessionNotFoundException {
        return sessionRepository.findById(sessionId).orElseThrow(() -> new SessionNotFoundException(sessionId));
    }

    private Session getSessionByIdWithMessages(Long sessionId) throws SessionNotFoundException {
        return sessionRepository.findByIdWithMessages(sessionId).orElseThrow(() -> new SessionNotFoundException(sessionId));
    }
}
