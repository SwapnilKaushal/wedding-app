package com.weddingapp.repository;

import com.weddingapp.entity.Event;
import com.weddingapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event,String> {

    List<Event> findByCreatedBy(User user);
}
