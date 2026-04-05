package com.weddingapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.PrivateKey;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="tasks")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event; // Many tasks belong to one event

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo; // Many tasks can be assigned to one user

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private LocalDate deadline;

    private boolean notified = false; //prevents duplicate deadline emails

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Status{
        PENDING,
        IN_PROGRESS,
        DONE
    }
}
