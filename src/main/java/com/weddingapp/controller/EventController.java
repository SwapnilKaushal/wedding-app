package com.weddingapp.controller;

import com.weddingapp.dto.EventRequest;
import com.weddingapp.dto.EventResponse;
import com.weddingapp.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponse> create(@Valid @RequestBody EventRequest request){
        return ResponseEntity.ok(eventService.createEvent(request));
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getMyEvents(){
        return ResponseEntity.ok(eventService.getMyEvents());
    }

    @GetMapping("{id}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable String id){
        return ResponseEntity.ok(eventService.getEvent(id));
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<Void> addMember(@PathVariable String id,
                                    @RequestBody Map<String,String> body){
        eventService.addMember(id,body.get("email"));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/members")
    public ResponseEntity<Void> removeMember(@PathVariable String id,
                                          @RequestBody Map<String,String> body){
        eventService.removeMember(id,body.get("email"));
        return ResponseEntity.noContent().build();
    }


}
