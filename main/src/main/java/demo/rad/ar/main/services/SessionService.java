package demo.rad.ar.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

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

    public Page<UserSession> getSessions(int page, int size, String search) {
        PageRequest pageable = PageRequest.of(page, size);
        if (search != null && !search.isEmpty()) {
            return sessionRepository.searchSessions(search, pageable);
        }
        return sessionRepository.findAll(pageable);
    }

    
    public List<UserSession> findUsersBySegment(@PathVariable Integer segmentId) {
        return sessionRepository.getUsersByUserSegment(segmentId);
    }
}