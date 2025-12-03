package com.bonterra.eventtracker.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.bonterra.eventtracker.entity.Event;
import com.bonterra.eventtracker.repository.EventRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class EventControllerTest {

        @Autowired
        private WebApplicationContext webApplicationContext;

        @Autowired
        private EventRepository eventRepository;

        private MockMvc mockMvc;

        @BeforeEach
        void setUp() {
                mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
                eventRepository.deleteAll();
        }

        @Test
        void testGetAllEvents_Success() throws Exception {
                Event event1 = new Event("Event 1", "Description 1",
                                LocalDateTime.of(2025, 12, 1, 9, 0),
                                LocalDateTime.of(2025, 12, 2, 17, 0),
                                "Location 1");
                Event event2 = new Event("Event 2", "Description 2",
                                LocalDateTime.of(2025, 12, 5, 9, 0),
                                LocalDateTime.of(2025, 12, 10, 17, 0),
                                "Location 2");

                eventRepository.save(event1);
                eventRepository.save(event2);

                mockMvc.perform(get("/api/events")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].name").value("Event 1"))
                                .andExpect(jsonPath("$[1].name").value("Event 2"));
        }

        @Test
        void testGetAllEvents_EmptyList() throws Exception {
                mockMvc.perform(get("/api/events")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        void testGetEventById_Success() throws Exception {
                Event event = new Event("Tech Summit", "Annual tech conference",
                                LocalDateTime.of(2025, 12, 15, 9, 0),
                                LocalDateTime.of(2025, 12, 17, 17, 0),
                                "Convention Center");
                Event savedEvent = eventRepository.save(event);

                mockMvc.perform(get("/api/events/{id}", savedEvent.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Tech Summit"))
                                .andExpect(jsonPath("$.description").value("Annual tech conference"));
        }

        @Test
        void testGetEventById_NotFound() throws Exception {
                mockMvc.perform(get("/api/events/{id}", "00000000-0000-0000-0000-000000000000")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testCreateEvent_Success() throws Exception {
                String eventJson = "{\"name\":\"New Event\",\"description\":\"New description\"," +
                                "\"startDate\":\"2025-12-01T10:00:00\",\"endDate\":\"2025-12-01T18:00:00\"," +
                                "\"location\":\"New Location\"}";

                mockMvc.perform(post("/api/events")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(eventJson))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.name").value("New Event"))
                                .andExpect(jsonPath("$.description").value("New description"));
        }

        @Test
        void testCreateEvent_FailsWhenDuplicateEventName() throws Exception {
                // Create first event
                Event event = new Event("Duplicate Event", "Description",
                                LocalDateTime.of(2025, 12, 1, 9, 0),
                                LocalDateTime.of(2025, 12, 1, 17, 0),
                                "Location");
                eventRepository.save(event);

                // Try to create another event with same name
                String eventJson = "{\"name\":\"Duplicate Event\",\"description\":\"Another description\"," +
                                "\"startDate\":\"2025-12-02T10:00:00\",\"endDate\":\"2025-12-02T18:00:00\"," +
                                "\"location\":\"Another Location\"}";

                mockMvc.perform(post("/api/events")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(eventJson))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.message").value("An event with this name already exists"));
        }

        @Test
        void testCreateEvent_SucceedsWithDifferentCaseName() throws Exception {
                // Create first event
                Event event = new Event("Test Event", "Description",
                                LocalDateTime.of(2025, 12, 1, 9, 0),
                                LocalDateTime.of(2025, 12, 1, 17, 0),
                                "Location");
                eventRepository.save(event);

                // Try to create event with different case - should still fail (case-insensitive
                // check)
                String eventJson = "{\"name\":\"test event\",\"description\":\"Another description\"," +
                                "\"startDate\":\"2025-12-02T10:00:00\",\"endDate\":\"2025-12-02T18:00:00\"," +
                                "\"location\":\"Another Location\"}";

                mockMvc.perform(post("/api/events")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(eventJson))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.message").value("An event with this name already exists"));
        }

        @Test
        void testCreateEvent_FailsWhenStartDateAfterEndDate() throws Exception {
                String eventJson = "{\"name\":\"New Event\",\"description\":\"New description\"," +
                                "\"startDate\":\"2025-12-02T10:00:00\",\"endDate\":\"2025-12-01T18:00:00\"," +
                                "\"location\":\"New Location\"}";

                mockMvc.perform(post("/api/events")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(eventJson))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void testUpdateEvent_Success() throws Exception {
                Event event = new Event("Old Name", "Old description",
                                LocalDateTime.of(2025, 12, 1, 9, 0),
                                LocalDateTime.of(2025, 12, 1, 17, 0),
                                "Old Location");
                Event savedEvent = eventRepository.save(event);

                String updateJson = "{\"name\":\"Updated Name\",\"description\":\"Updated description\"," +
                                "\"startDate\":\"2025-12-01T09:00:00\",\"endDate\":\"2025-12-01T17:00:00\"," +
                                "\"location\":\"Updated Location\",\"minAttendees\":50,\"maxAttendees\":500}";

                mockMvc.perform(put("/api/events/{id}", savedEvent.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateJson))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Updated Name"))
                                .andExpect(jsonPath("$.description").value("Updated description"));
        }

        @Test
        void testUpdateEvent_FailsWhenUpdatingToExistingName() throws Exception {
                // Create two events
                Event event1 = new Event("Event 1", "Description 1",
                                LocalDateTime.of(2025, 12, 1, 9, 0),
                                LocalDateTime.of(2025, 12, 1, 17, 0),
                                "Location 1");
                eventRepository.save(event1);

                Event event2 = new Event("Event 2", "Description 2",
                                LocalDateTime.of(2025, 12, 2, 9, 0),
                                LocalDateTime.of(2025, 12, 2, 17, 0),
                                "Location 2");
                Event savedEvent2 = eventRepository.save(event2);

                // Try to update event2 with event1's name
                String updateJson = "{\"name\":\"Event 1\",\"description\":\"Updated description\"," +
                                "\"startDate\":\"2025-12-02T09:00:00\",\"endDate\":\"2025-12-02T17:00:00\"," +
                                "\"location\":\"Updated Location\"}";

                mockMvc.perform(put("/api/events/{id}", savedEvent2.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateJson))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.message").value("An event with this name already exists"));
        }

        @Test
        void testUpdateEvent_SucceedsWhenKeepingSameName() throws Exception {
                // Create an event
                Event event = new Event("Event Name", "Old description",
                                LocalDateTime.of(2025, 12, 1, 9, 0),
                                LocalDateTime.of(2025, 12, 1, 17, 0),
                                "Old Location");
                Event savedEvent = eventRepository.save(event);

                // Update the event but keep the same name (should succeed)
                String updateJson = "{\"name\":\"Event Name\",\"description\":\"Updated description\"," +
                                "\"startDate\":\"2025-12-01T09:00:00\",\"endDate\":\"2025-12-01T17:00:00\"," +
                                "\"location\":\"Updated Location\"}";

                mockMvc.perform(put("/api/events/{id}", savedEvent.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateJson))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Event Name"))
                                .andExpect(jsonPath("$.description").value("Updated description"));
        }

        @Test
        void testUpdateEvent_FailsWhenUpdatingToExistingNameCaseInsensitive() throws Exception {
                // Create two events
                Event event1 = new Event("Test Event", "Description 1",
                                LocalDateTime.of(2025, 12, 1, 9, 0),
                                LocalDateTime.of(2025, 12, 1, 17, 0),
                                "Location 1");
                eventRepository.save(event1);

                Event event2 = new Event("Another Event", "Description 2",
                                LocalDateTime.of(2025, 12, 2, 9, 0),
                                LocalDateTime.of(2025, 12, 2, 17, 0),
                                "Location 2");
                Event savedEvent2 = eventRepository.save(event2);

                // Try to update event2 with event1's name but different case - should fail
                // (case-insensitive)
                String updateJson = "{\"name\":\"test event\",\"description\":\"Updated description\"," +
                                "\"startDate\":\"2025-12-02T09:00:00\",\"endDate\":\"2025-12-02T17:00:00\"," +
                                "\"location\":\"Updated Location\"}";

                mockMvc.perform(put("/api/events/{id}", savedEvent2.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateJson))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.message").value("An event with this name already exists"));
        }

        @Test
        void testUpdateEvent_NotFound() throws Exception {
                String updateJson = "{\"name\":\"Updated Name\",\"description\":\"Updated\"," +
                                "\"startDate\":\"2025-12-01T09:00:00\",\"endDate\":\"2025-12-01T17:00:00\"," +
                                "\"location\":\"Location\"}";

                mockMvc.perform(put("/api/events/{id}", "00000000-0000-0000-0000-000000000000")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateJson))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testDeleteEvent_Success() throws Exception {
                Event event = new Event("Event to Delete", "Will be deleted",
                                LocalDateTime.of(2025, 12, 1, 9, 0),
                                LocalDateTime.of(2025, 12, 1, 17, 0),
                                "Location");
                Event savedEvent = eventRepository.save(event);

                mockMvc.perform(delete("/api/events/{id}", savedEvent.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNoContent());

                mockMvc.perform(get("/api/events/{id}", savedEvent.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testDeleteEvent_NotFound() throws Exception {
                mockMvc.perform(delete("/api/events/{id}", "00000000-0000-0000-0000-000000000000")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }
}
