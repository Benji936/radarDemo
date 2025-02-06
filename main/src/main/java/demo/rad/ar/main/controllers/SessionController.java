package demo.rad.ar.main.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import demo.rad.ar.main.models.UserSession;
import demo.rad.ar.main.services.SessionService;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    @Autowired
    private SessionService sessionService;

    @GetMapping
    public List<UserSession> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @PostMapping
    public UserSession createSession(@RequestBody UserSession session) {
        return sessionService.saveSession(session);
    }
}
