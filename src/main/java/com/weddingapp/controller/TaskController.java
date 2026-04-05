package com.weddingapp.controller;

import com.weddingapp.dto.TaskRequest;
import com.weddingapp.dto.TaskResponse;
import com.weddingapp.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> create( @Valid @RequestBody TaskRequest request){
        return ResponseEntity.ok(taskService.createTask(request));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<TaskResponse>> getByEvent(@PathVariable String eventId){
        return ResponseEntity.ok(taskService.getTasksByEvent(eventId));
    }

    @PatchMapping("{id}/status")
    public ResponseEntity<TaskResponse> updateStatus(@PathVariable String taskId,
                                                     @RequestBody Map<String,String> request){
        return ResponseEntity.ok(taskService.updateStatus(taskId,request.get("status")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(@PathVariable String taskId,
                                               @RequestBody TaskRequest request){
        return ResponseEntity.ok(taskService.updateTask(taskId,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String taskId){
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
