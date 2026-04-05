package com.weddingapp.service;

import com.weddingapp.dto.TaskRequest;
import com.weddingapp.dto.TaskResponse;
import com.weddingapp.entity.Event;
import com.weddingapp.entity.Task;
import com.weddingapp.entity.User;
import com.weddingapp.repository.EventRepository;
import com.weddingapp.repository.TaskRepository;
import com.weddingapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TaskRepository taskRepository;

    public TaskResponse createTask(TaskRequest request) {
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        User assignedTo = null;
        if (request.getAssignedToEmail() != null){
            assignedTo = userRepository.findByEmail(request.getAssignedToEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
        Task task = Task.builder()
                .assignedTo(assignedTo)
                .title(request.getTitle())
                .description(request.getDescription())
                .deadline(request.getDeadline())
                .event(event)
                .status(Task.Status.PENDING)
                .build();
        return toResponse(task);
    }

    public List<TaskResponse> getTasksByEvent(String eventId){
        return taskRepository.findByEventId(eventId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse updateStatus(String taskId, String status){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(()->new RuntimeException("Task not found"));

        task.setStatus(Task.Status.valueOf(status.toUpperCase()));
        return toResponse(taskRepository.save(task));
    }

    public TaskResponse updateTask(String taskId, TaskRequest request){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(()->new RuntimeException("Task not found"));
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDeadline(request.getDeadline());

        if(request.getAssignedToEmail()!=null){
           User assignedTo = userRepository.findByEmail(request.getAssignedToEmail())
                   .orElseThrow(()-> new RuntimeException("User not found"));
           task.setAssignedTo(assignedTo);
        }

        return toResponse(taskRepository.save(task));
    }

    public void deleteTask(String taskId) {
        taskRepository.deleteById(taskId);
    }





    /*
            Helper Methods
     */

    public TaskResponse toResponse(Task t){
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(t.getId());
        taskResponse.setTitle(t.getTitle());
        taskResponse.setDescription(t.getDescription());
        taskResponse.setStatus(t.getStatus().name());
        taskResponse.setCreatedAt(t.getCreatedAt());
        taskResponse.setEventId(t.getEvent().getId());
        taskResponse.setDeadline(t.getDeadline());
        if (t.getAssignedTo() != null) {
            taskResponse.setAssignedToName(t.getAssignedTo().getName());
            taskResponse.setAssignedToEmail(t.getAssignedTo().getEmail());
        }
        return taskResponse;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
