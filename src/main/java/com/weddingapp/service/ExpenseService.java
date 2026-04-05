package com.weddingapp.service;

import com.weddingapp.dto.ExpenseRequest;
import com.weddingapp.dto.ExpenseResponse;
import com.weddingapp.entity.Event;
import com.weddingapp.entity.Expense;
import com.weddingapp.entity.User;
import com.weddingapp.repository.EventRepository;
import com.weddingapp.repository.ExpenseRepository;
import com.weddingapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public ExpenseResponse addExpense(ExpenseRequest request){
        User currentUser = getCurrentUser();
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(()->new RuntimeException(" Event not found "));
        Expense expense = Expense.builder()
                .title(request.getTitle())
                .amount(request.getAmount())
                .category(request.getCategory())
                .receiptUrl(request.getReceiptUrl())
                .event(event)
                .addedBy(currentUser)
                .build();

        return toResponse(expenseRepository.save(expense));
    }

    public List<ExpenseResponse> getExpensesByEvent(String evenId){
        return expenseRepository.findByEventId(evenId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public Map<String, BigDecimal> getCategoryTotal(String eventId){
        return expenseRepository.findByEventId(eventId)
                .stream()
                .collect(Collectors.groupingBy(
                        e->e.getCategory() !=null?e.getCategory() :"Uncategorised"
                        , Collectors.reducing(
                                BigDecimal.ZERO,
                                Expense::getAmount,
                                BigDecimal::add)
                ));
        /* Same as below loop
        Map<String, BigDecimal> result = new HashMap<>();

        for (Expense e : expenseRepository.findByEventId(eventId)) {
            String category = e.getCategory() != null ? e.getCategory() : "Uncategorised";

            result.put(
                category,
                result.getOrDefault(category, BigDecimal.ZERO).add(e.getAmount())
            );
        }

        return result;
 */
    }

    public BigDecimal getEventTotal(String eventId){
        return expenseRepository.findByEventId(eventId)
                .stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        /*
        Same as below loop :
        .map(Expense::getAmount) --> Extract Amount [Expense, Expense, Expense] → [1000, 2000, 5000]
        .reduce(BigDecimal.ZERO, BigDecimal::add) Start with 0 Keep adding all amounts
         0 + 1000 + 2000 + 5000 = 8000

        Similar Code using loop :
                BigDecimal total = BigDecimal.ZERO;

                for (Expense e : expenseRepository.findByEventId(eventId)) {
                    total = total.add(e.getAmount());
                }

                return total;
         */
    }

    public void deleteExpense(String expenseId) {
        expenseRepository.deleteById(expenseId);
    }

    public ExpenseResponse updateExpense(String expenseId , ExpenseRequest request){
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(()-> new RuntimeException("Expense not Found"));
        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setReceiptUrl(request.getReceiptUrl());

        return toResponse(expenseRepository.save(expense));
    }


    /*
            Helper Methods
     */

    private ExpenseResponse toResponse(Expense e){
        ExpenseResponse er = new ExpenseResponse();
        er.setId(e.getId());
        er.setCategory(e.getCategory());
        er.setAmount(e.getAmount());
        er.setTitle(e.getTitle());
        er.setReceiptUrl(e.getReceiptUrl());
        er.setAddedByName(e.getAddedBy().getName());
        er.setAddedByEmail(e.getAddedBy().getEmail());
        er.setEventId(e.getEvent().getId());
        er.setCreatedAt(e.getCreatedAt());

        return er;
    }


    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
