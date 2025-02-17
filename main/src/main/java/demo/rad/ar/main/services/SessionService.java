package demo.rad.ar.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import demo.rad.ar.main.models.UserSession;
import demo.rad.ar.main.repository.SessionRepository;
import smile.data.DataFrame;
import smile.data.formula.Formula;
import smile.classification.RandomForest;
import smile.io.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    @Cacheable("sessions")
    public List<UserSession> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Optional<UserSession> getSessionById(long id){
        return sessionRepository.findById(id);
    }

    public UserSession saveSession(UserSession session) {
        return sessionRepository.save(session);
    }

    public Page<UserSession> getSessions(int page, int size, String search, Integer segment) {
        PageRequest pageable = PageRequest.of(page, size);

        if (segment != null) {
            System.out.println("---- By Segment ----");
            return sessionRepository.getSessionsByUserSegment(segment, pageable);
            
        }

        if (search != null && !search.isEmpty()) {
            System.out.println("---- By Search ----");
            return sessionRepository.searchSessions(search, pageable);
        }

        System.out.println("---- All ----");
        System.out.println(segment);
        System.out.println(search);
        return sessionRepository.findAll(pageable);
    }

    private RandomForest model;
    private static final String MODEL_PATH = "session_model.rf";
    private Map<Integer, String> productIdDecoding = new HashMap<>();
    
    public void trainModel() {
        List<UserSession> sessions = sessionRepository.findAll();
        
        if (sessions.isEmpty()) {
            System.out.println("No data available for training.");
            return;
        }
        
        int[][] features = new int[sessions.size()][];
        String[] labels = new String[sessions.size()];
        
        for (int i = 0; i < sessions.size(); i++) {
            UserSession session = sessions.get(i);
            features[i] = extractFeatures(session);
            
            String productId = session.getProductId();
            labels[i] = productId;
        }
        
        DataFrame df = DataFrame.of(features,"City","DayOfWeek","Device","Browser","DeviceBrand","Language","DayOfMonth","productId");
        model = RandomForest.fit(Formula.lhs("productId"),df);
        
        try {
            Write.object(model, Paths.get(MODEL_PATH));
            System.out.println("Model saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String predict(UserSession session) {
        if (model == null) {
            try {
                model = (RandomForest) Read.object(Paths.get(MODEL_PATH));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
        
        int[] features = extractFeatures(session);
        DataFrame predictionData = DataFrame.of(new int[][]{features});
        int predictedIndex = model.predict(predictionData)[0];
        return productIdDecoding.getOrDefault(predictedIndex, "Unknown");
    }
    
    private int[] extractFeatures(UserSession session) {
        return new int[]{
            encodeCategorical(session.getCity()),
            encodeCategorical(session.getDayOfWeek()),
            encodeCategorical(session.getDevice()),
            encodeCategorical(session.getBrowser()),
            encodeCategorical(session.getDeviceBrand()),
            encodeCategorical(session.getLanguage()),
            session.getDayOfMonth(),
            encodeCategorical(session.getProductId()),
        };
    }

    private int encodeCategorical(String value) {
        return value == null ? -1 : value.hashCode();
    }
}