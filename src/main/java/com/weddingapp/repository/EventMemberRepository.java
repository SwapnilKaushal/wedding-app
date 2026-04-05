package com.weddingapp.repository;

import com.weddingapp.entity.Event;
import com.weddingapp.entity.EventMember;
import com.weddingapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventMemberRepository extends JpaRepository<EventMember,String> {
    List<EventMember> findByUser(User user); // User is part of which all events
    List<EventMember> findByEvent(Event event); // all members of an Event
    boolean existsByEventAndUser(Event event, User user); // If user joined the event or not
    void deleteByEventAndUser(Event event, User user); // Remove user from Event
}
