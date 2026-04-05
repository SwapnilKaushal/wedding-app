package com.weddingapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TaskRequest {
    @NotBlank
    private String title;

    private String description;
    private LocalDate deadline;
    private String assignedToEmail;

    @NotBlank
    private String eventId;
}
