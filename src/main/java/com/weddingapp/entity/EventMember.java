package com.weddingapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_members")
@Builder @AllArgsConstructor @NoArgsConstructor @Data
public class EventMember {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event; // many event_members belong to single event

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user; // Many EventMember records → belong to one User
                        // Because same user can join multiple events

    private LocalDateTime joinedAt = LocalDateTime.now();
}
