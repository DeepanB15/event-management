package com.example.backend.controller;

import com.example.backend.model.Event;
import com.example.backend.model.User;
import com.example.backend.repository.EventRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") // allow frontend localhost
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    // 🔹 Login or create user
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        Optional<User> existing = userRepository.findByEmail(user.getEmail());
        if (existing.isPresent()) {
            return "Login successful!";
        } else {
            userRepository.save(user);
            return "User created and login successful!";
        }
    }

    // 🔹 Get all events
    @GetMapping("/events")
    public Iterable<Event> getEvents() {
        return eventRepository.findAll();
    }

    // 🔹 Create new event
    @PostMapping("/events")
    public String createEvent(@RequestBody Event event) {
        eventRepository.save(event);
        return "New event created successfully!";
    }

    // 🔹 Register for an event
    @PostMapping("/events/{id}/register")
    public String registerEvent(@PathVariable String id, @RequestParam String email) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        if (!event.getAttendees().contains(email)) {
            event.getAttendees().add(email);
            eventRepository.save(event);
            return "Event registration successful!";
        }
        return "Already registered!";
    }
}
