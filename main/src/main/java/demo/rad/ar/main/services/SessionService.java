package demo.rad.ar.main.services;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import demo.rad.ar.main.models.UserSession;
import demo.rad.ar.main.repository.SessionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

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

    private static final SparkSession spark = SparkSession.builder()
        .appName("UserSessionRecommendation")
        .master("local[*]")  // Run Spark locally
        .getOrCreate();

    public SparkSession getSparkSession() {
        return spark;
    }

    public Dataset<Row> loadUserSessionData() {
        List<UserSession> sessions = sessionRepository.findAll();
    
        // Convert list to DataFrame
        Dataset<Row> sessionDF = spark.createDataFrame(sessions, UserSession.class);
        sessionDF.show(); // Debugging: Show first few rows
    
        return sessionDF;
    }
}