package com.weddingapp.dto;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TaskResponse {
    private String id;
    private String title;
    private String description;
    private LocalDate deadline;
    private String assignedToName;
    private String assignedToEmail;
    private String status;
    private String eventId;
    private LocalDateTime createdAt;
}
