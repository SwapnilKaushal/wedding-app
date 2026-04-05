package com.weddingapp.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventResponse {

    private String id;
    private String name;
    private String description;
    private LocalDateTime weddingDate;
    private String createdByName;
    private List<String> memberEmail;
    private LocalDateTime createdAt;

}
