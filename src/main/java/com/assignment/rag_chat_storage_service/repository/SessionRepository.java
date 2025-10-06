package com.assignment.rag_chat_storage_service.repository;

import com.assignment.rag_chat_storage_service.model.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Session findByTitle(String title);

    @Query("SELECT s FROM Session s LEFT JOIN FETCH s.messages WHERE s.id = :id")
    Optional<Session> findByIdWithMessages(@Param("id") Long id);

    @Query("SELECT s FROM Session s WHERE s.id = :id")
    Optional<Session> findByIdWithoutMessages(@Param("id") Long id);

    @Query("SELECT s FROM Session s")
    Page<Session> findAllWithoutMessages(Pageable pageable);
}
