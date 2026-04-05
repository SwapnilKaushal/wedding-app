package com.weddingapp.repository;

import com.weddingapp.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,String> {

    List<Task> findByEventId(String eventId);

    List<Task> findByDeadlineBeforeAndNotifiedFalseAndStatusNot(
            LocalDate date, Task.Status status);  // for deadline scheduler
}
