package demo.rad.ar.main.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import demo.rad.ar.main.models.UserSession;
import demo.rad.ar.main.services.SessionService;

import java.util.List;

@RestController
@CrossOrigin()
@RequestMapping("/api/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @CrossOrigin()
    @GetMapping("/all")
    public List<UserSession> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @PostMapping
    public UserSession createSession(@RequestBody UserSession session) {
        return sessionService.saveSession(session);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<UserSession>>> getSessions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            PagedResourcesAssembler<UserSession> assembler) {

        Page<UserSession> resultPage = sessionService.getSessions(page, size, search);
        return ResponseEntity.ok(assembler.toModel(resultPage));
    }

    @GetMapping("/segments/{segmentId}")
    public List<UserSession> getSegment(@PathVariable Integer segmentId) {
        return sessionService.findUsersBySegment(segmentId);
    }

    
}
