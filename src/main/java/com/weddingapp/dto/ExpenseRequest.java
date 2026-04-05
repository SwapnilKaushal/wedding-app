package com.weddingapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExpenseRequest {

    @NotBlank
    private String title;

    @NotNull
    private BigDecimal amount;

    private String category;
    private String receiptUrl;

    @NotBlank
    private String eventId;


}
