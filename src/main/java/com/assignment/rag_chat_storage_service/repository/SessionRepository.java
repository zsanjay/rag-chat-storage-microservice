package com.assignment.rag_chat_storage_service.repository;

import com.assignment.rag_chat_storage_service.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Session findByTitle(String title);
}
