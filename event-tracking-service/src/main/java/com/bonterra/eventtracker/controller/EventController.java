package com.bonterra.eventtracker.controller;

import com.bonterra.eventtracker.entity.Event;
import com.bonterra.eventtracker.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable UUID id) {
        Optional<Event> event = eventRepository.findById(id);
        return event.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody Event event) {
        // Check if event with same name already exists
        if (eventRepository.findByNameIgnoreCase(event.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("An event with this name already exists"));
        }

        // Validate that start date is before end date
        if (event.getStartDate() != null && event.getEndDate() != null &&
                event.getStartDate().isAfter(event.getEndDate())) {
            return ResponseEntity.badRequest().build();
        }
        Event savedEvent = eventRepository.save(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable UUID id, @RequestBody Event eventDetails) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();

            // Check if trying to update to a name that already exists (and it's a different
            // event)
            if (!event.getName().equalsIgnoreCase(eventDetails.getName())) {
                if (eventRepository.findByNameIgnoreCase(eventDetails.getName()).isPresent()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(new ErrorResponse("An event with this name already exists"));
                }
            }

            event.setName(eventDetails.getName());
            event.setDescription(eventDetails.getDescription());
            event.setStartDate(eventDetails.getStartDate());
            event.setEndDate(eventDetails.getEndDate());
            event.setLocation(eventDetails.getLocation());
            event.setMinAttendees(eventDetails.getMinAttendees());
            event.setMaxAttendees(eventDetails.getMaxAttendees());
            event.setLocationNotes(eventDetails.getLocationNotes());
            event.setPreparationNotes(eventDetails.getPreparationNotes());
            Event updatedEvent = eventRepository.save(event);
            return ResponseEntity.ok(updatedEvent);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
