package demo.rad.ar.main.repository;


import demo.rad.ar.main.models.UserSession;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<UserSession, Long> {

    // Search in browser, OS, and city fields
    @Query("SELECT u FROM UserSession u WHERE " +
            "LOWER(u.browser) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.os) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.city) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<UserSession> searchSessions(@Param("search") String search, Pageable pageable);

    public List<UserSession> getUsersByUserSegment(Integer segmentId);
}
