package demo.rad.ar.main.repository;


import demo.rad.ar.main.models.UserSession;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<UserSession, Long>, JpaSpecificationExecutor<UserSession>{

    // Search in browser, OS, and city fields
    @Query("SELECT u FROM UserSession u WHERE " +
            "LOWER(u.browser) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.os) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.city) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<UserSession> searchSessions(@Param("search") String search, Pageable pageable);

    @Query("SELECT u FROM UserSession u WHERE u.userSegment=:segment")
    Page<UserSession> getSessionsByUserSegment(@Param("segment") Integer segment, Pageable pageable);

    @Query("SELECT u.dayOfWeek, u.sessionHour, u.dayOfMonth, u.season, u.isHoliday, u.refererUrl, u.refererDomain, u.productId, u.productTag, u.deviceType, u.deviceBrand, u.os, u.screenDimensions, u.isDay, u.isTouchCapable, u.browser, u.language FROM UserSession u")
    List<UserSession> getAllSessionsForSegment();
}
