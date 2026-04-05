package com.weddingapp.repository;


import com.weddingapp.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense,String> {

    List<Expense> findByEventId(String eventId);

    List<Expense> findByEventIdAndCategory(String eventId,String category);
}
