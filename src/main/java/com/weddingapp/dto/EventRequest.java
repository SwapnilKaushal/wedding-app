package com.weddingapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventRequest {

    @NotBlank
    private String name;

    private LocalDateTime weddingDate;

    private String description;

}
