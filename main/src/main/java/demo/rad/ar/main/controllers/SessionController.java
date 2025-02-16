package demo.rad.ar.main.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import demo.rad.ar.main.models.UserSession;
import demo.rad.ar.main.services.SessionService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin()
@RequestMapping("/api/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;
    //private AIService aIService;

    @CrossOrigin()
    @GetMapping("/all")
    public List<UserSession> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserSession> getSessionById(@PathVariable Long id) {
        Optional<UserSession> session = sessionService.getSessionById(id);
        return session.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
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
            @RequestParam(required = false) Integer segment,
            PagedResourcesAssembler<UserSession> assembler) {

        Page<UserSession> resultPage = sessionService.getSessions(page, size, search, segment);
        return ResponseEntity.ok(assembler.toModel(resultPage));
    }

    @GetMapping("/train-model")
    public ResponseEntity<String> trainModel() {
        try{
            sessionService.trainModel();
            return ResponseEntity.ok("Training Done !");
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body("Errors: " + e.getMessage());
        }
    }

    @GetMapping("/predict/{sessionsId}")
    public ResponseEntity<String> predictProduct(@PathVariable long sessionId){
        try{
            UserSession userSession = sessionService.getSessionById(sessionId).get();
            String prediction = sessionService.predict(userSession);
            
            return ResponseEntity.ok(prediction);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body("Errors: " + e.getMessage());
        }
    }
}
