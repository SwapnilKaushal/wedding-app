package com.weddingapp.service;

import com.weddingapp.dto.EventRequest;
import com.weddingapp.dto.EventResponse;
import com.weddingapp.entity.Event;
import com.weddingapp.entity.EventMember;
import com.weddingapp.entity.User;
import com.weddingapp.repository.EventMemberRepository;
import com.weddingapp.repository.EventRepository;
import com.weddingapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMemberRepository eventMemberRepository;

    public EventResponse createEvent(EventRequest eventRequest){
        User currentUser = getCurrentUser();
        Event event = Event.builder()
                .name(eventRequest.getName())
                .weddingDate(eventRequest.getWeddingDate())
                .description(eventRequest.getDescription())
                .createdBy(currentUser)
                .build();
        Event saved = eventRepository.save(event);
        // auto Add creator as Member
        EventMember member = EventMember.builder()
                .event(saved)
                .user(currentUser)
                .build();
        eventMemberRepository.save(member);

        return toResponse(saved);
    }

    public List<EventResponse> getMyEvents(){
        User currentUser = getCurrentUser();
        return eventMemberRepository.findByUser(currentUser)
                .stream()
                .map(m->toResponse(m.getEvent()))
                .collect(Collectors.toList());
    }

    public EventResponse getEvent(String eventId){
        Event e =  eventRepository.findById(eventId)
                .orElseThrow(()-> new RuntimeException ("Event not found"));

        return toResponse(e);
    }

    public void addMember(String eventId, String memberEmail){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(()-> new RuntimeException("Event Not Found"));
        User user = userRepository.findByEmail(memberEmail)
                .orElseThrow(()-> new RuntimeException("User not Found"));
        Boolean alreadyMember = eventMemberRepository.existsByEventAndUser(event,user);
        if(alreadyMember)
            throw new RuntimeException("User is already a member");

        eventMemberRepository.save(EventMember.builder()
                .event(event)
                .user(user)
                .build());
    }

    public void removeMember(String eventId,String email){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(()-> new RuntimeException("Event Not Found"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not Found"));
        eventMemberRepository.deleteByEventAndUser(event,user);
    }




 /*
            Helper Methods
  */

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private EventResponse toResponse(Event e){
        EventResponse er = new EventResponse();
        er.setId(e.getId());
        er.setCreatedAt(e.getCreatedAt());
        er.setName(e.getName());
        er.setDescription(e.getDescription());
        er.setName(e.getName());
        er.setCreatedByName(e.getCreatedBy().getName());
        er.setWeddingDate(e.getWeddingDate());
        er.setCreatedAt(e.getCreatedAt());
        List<EventMember> memberList = eventMemberRepository.findByEvent(e);
        er.setMemberEmail(memberList.stream()
                .map(m->m.getUser().getEmail())
                .collect(Collectors.toList()));

        return er;
    }
}
