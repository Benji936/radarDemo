package demo.rad.ar.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.rad.ar.main.models.UserSession;
import demo.rad.ar.main.repository.SessionRepository;

import java.util.List;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    public List<UserSession> getAllSessions() {
        return sessionRepository.findAll();
    }

    public UserSession saveSession(UserSession session) {
        return sessionRepository.save(session);
    }
}