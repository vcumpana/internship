package com.endava.service_system.dao;

import com.endava.service_system.model.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface NotificationDao extends JpaRepository<Notification, Long> {
    @Query("SELECT n FROM Notification AS n JOIN n.recipient AS r WHERE r.username = :username AND n.notificationStatus = 'UNREAD'")
    public List<Notification> getListOfUnreadNotification(@Param("username") String username);
}
