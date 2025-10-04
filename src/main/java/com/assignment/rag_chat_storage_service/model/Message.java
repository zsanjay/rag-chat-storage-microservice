package com.assignment.rag_chat_storage_service.model;

import com.assignment.rag_chat_storage_service.constant.SenderType;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_type", nullable = false)
    private SenderType senderType;

    @Lob
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    @Basic(fetch = FetchType.EAGER)
    private String content;
}
