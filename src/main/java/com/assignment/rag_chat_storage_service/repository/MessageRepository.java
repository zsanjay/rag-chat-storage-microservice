package com.assignment.rag_chat_storage_service.repository;

import com.assignment.rag_chat_storage_service.model.Message;
import com.assignment.rag_chat_storage_service.model.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findAllBySession(Session session, Pageable pageable);
}
