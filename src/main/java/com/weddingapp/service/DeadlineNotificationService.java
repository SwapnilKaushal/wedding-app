package com.weddingapp.service;

import com.weddingapp.entity.Task;
import com.weddingapp.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeadlineNotificationService {

    private final TaskRepository taskRepository;
    private final JavaMailSender mailSender;

    @Scheduled(cron = "0 0 8 * * *")
    public void sendDeadlineReminder(){
        LocalDate twoDaysFormNow = LocalDate.now().plusDays(2);
        List<Task> upcomingTasks = taskRepository
                .findByDeadlineBeforeAndNotifiedFalseAndStatusNot(twoDaysFormNow,Task.Status.DONE);

        for(Task task : upcomingTasks){
            if(task.getAssignedTo()!=null){
                try{
                    sendReminderEmail(task);
                    task.setNotified(true);
                    taskRepository.save(task);
                    log.info("Deadline reminder sent for task: {}", task.getTitle());
                } catch (Exception e) {
                    log.error("Failed to send reminder for task {}: {}", task.getId(), e.getMessage());
                }
            }
        }
    }

    public void sendReminderEmail(Task task){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(task.getAssignedTo().getEmail());
        message.setSubject("Reminder: Task due soon - "+ task.getTitle());
        message.setText(
                "Hi " + task.getAssignedTo().getName() + ",\n\n" +
                        "This is a reminder that the following task is due on " + task.getDeadline() + ":\n\n" +
                        "Task: " + task.getTitle() + "\n" +
                        (task.getDescription() != null ? "Details: " + task.getDescription() + "\n" : "") +
                        "Event: " + task.getEvent().getName() + "\n\n" +
                        "Please make sure to complete it on time.\n\n" +
                        "— Wedding App"
        );
        mailSender.send(message);
    }
}
