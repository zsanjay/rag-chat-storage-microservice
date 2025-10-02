package com.assignment.rag_chat_storage_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sessions")
public class Session extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id",unique = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String title;

    @Column(name = "is_favorite")
    private boolean isFavorite;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    public Session(String title, boolean isFavorite) {
        this.title = title;
        this.isFavorite = isFavorite;
    }
}
