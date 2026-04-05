package com.weddingapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event; // Many expenses belong to one event

    @ManyToOne
    @JoinColumn(name="added_by")
    private User addedBy; // Many expenses can be added by one User

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private BigDecimal amount;

    private String category; // Decor,Food,Photography ...

    private String receiptUrl; // pre-signed URL link

    private LocalDateTime createdAt = LocalDateTime.now();

}
