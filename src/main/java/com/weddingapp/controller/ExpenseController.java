package com.weddingapp.controller;

import com.weddingapp.dto.ExpenseRequest;
import com.weddingapp.dto.ExpenseResponse;
import com.weddingapp.entity.Event;
import com.weddingapp.entity.Expense;
import com.weddingapp.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponse> addExpense(@Valid @RequestBody ExpenseRequest request){
        return ResponseEntity.ok(expenseService.addExpense(request));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<ExpenseResponse>> getByEvent(@PathVariable String eventId){
        return ResponseEntity.ok(expenseService.getExpensesByEvent(eventId));
    }

    @GetMapping("/event/{eventId}/total")
    public ResponseEntity<BigDecimal> getTotal(@PathVariable String eventId){
        return ResponseEntity.ok(expenseService.getEventTotal(eventId));
    }

    @GetMapping("/event/{eventId}/by-category")
    public ResponseEntity<Map<String,BigDecimal>> getByCategory(@PathVariable String eventId){
        return ResponseEntity.ok(expenseService.getCategoryTotal(eventId));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String expenseId){
        expenseService.deleteExpense(expenseId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateEvent(@PathVariable String expId,
                                         @Valid ExpenseRequest request){
        return ResponseEntity.ok(expenseService.updateExpense(expId,request));
    }
}
