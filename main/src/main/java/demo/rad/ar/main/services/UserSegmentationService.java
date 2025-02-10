package demo.rad.ar.main.services;

import demo.rad.ar.main.models.UserSession;
import demo.rad.ar.main.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smile.clustering.KMeans;

import java.util.List;

@Service
public class UserSegmentationService {

    @Autowired
    private SessionRepository sessionRepository;

    public void performSegmentation() {
        List<UserSession> sessions = sessionRepository.findAll();
        if (sessions.isEmpty()) {
            System.out.println("No session data available for clustering.");
            return;
        }

        // Extract features (Example: device type, OS, browser)
        double[][] featureMatrix = new double[sessions.size()][3];
        for (int i = 0; i < sessions.size(); i++) {
            UserSession session = sessions.get(i);
            featureMatrix[i][0] = session.getDeviceType().equals("mobile") ? 1.0 : 0.0; // Mobile = 1, Others = 0
            featureMatrix[i][1] = session.getOs().equalsIgnoreCase("Android") ? 1.0 : 0.0; // Android = 1
            featureMatrix[i][2] = session.getBrowser().equalsIgnoreCase("Chrome") ? 1.0 : 0.0; // Chrome = 1
        }

        // Apply K-Means clustering (3 clusters)
        KMeans kmeans = KMeans.fit(featureMatrix, 3);

        // Assign cluster labels to users
        for (int i = 0; i < sessions.size(); i++) {
            sessions.get(i).setUserSegment(kmeans.y[i]); // Save cluster label
            sessionRepository.save(sessions.get(i));
        }

        System.out.println("User segmentation completed!");
    }
}

