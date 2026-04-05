package com.weddingapp.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExpenseResponse {

    private String id;
    private String title;
    private BigDecimal amount;
    private String category;
    private String receiptUrl;
    private String addedByName;
    private String addedByEmail;
    private String eventId;
    private LocalDateTime createdAt;
}
