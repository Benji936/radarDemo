package demo.rad.ar.main.services;

import demo.rad.ar.main.models.UserSession;
import demo.rad.ar.main.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smile.clustering.KMeans;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class UserSegmentationService {

    @Autowired
    private SessionRepository sessionRepository;

    public void performSegmentation(List<String> selectedAttributes, int numClusters) {
        List<UserSession> sessions = sessionRepository.findAll();
        if (sessions.isEmpty() || selectedAttributes.isEmpty()) {
            System.out.println("No session data or attributes selected for clustering.");
            return;
        }

        int numAttributes = selectedAttributes.size();
        double[][] featureMatrix = new double[sessions.size()][numAttributes];

        // Dynamically build feature matrix based on selected attributes
        for (int i = 0; i < sessions.size(); i++) {
            UserSession session = sessions.get(i);
            for (int j = 0; j < numAttributes; j++) {
                featureMatrix[i][j] = getNumericValue(session, selectedAttributes.get(j));
            }
        }

        // Apply K-Means clustering with a dynamic number of clusters
        KMeans kmeans = KMeans.fit(featureMatrix, numClusters);

        // Update user segment in DB
        for (int i = 0; i < sessions.size(); i++) {
            sessions.get(i).setUserSegment(kmeans.y[i]);
            sessionRepository.save(sessions.get(i));
        }

        System.out.println("Dynamic user segmentation completed!");
    }

    // Convert selected attribute to a numeric value
    private double getNumericValue(UserSession session, String attribute) {
        try {
            Field field = UserSession.class.getDeclaredField(attribute);
            field.setAccessible(true);
            Object value = field.get(session);
            if (value instanceof Number) return ((Number) value).doubleValue();
            if (value instanceof Boolean) return (Boolean) value ? 1.0 : 0.0;
            if (value instanceof String) return value.toString().hashCode() % 1000; // Hash strings to numbers
        } catch (Exception e) {
            System.err.println("Error processing attribute: " + attribute);
        }
        return 0.0;
    }
}

