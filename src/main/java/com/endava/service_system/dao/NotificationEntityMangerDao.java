package com.endava.service_system.dao;

import com.endava.service_system.model.entities.Notification;
import com.endava.service_system.model.enums.NotificationStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class NotificationEntityMangerDao {
    @PersistenceContext
    private EntityManager entityManager;
    private int PAGE_SIZE = 10;

    public List<Notification> getAllNotificationsForUser(String username, NotificationStatus status, int page){
        String hql = "SELECT n FROM Notification AS n JOIN n.recipient AS r WHERE r.username = :username ";
        if(status != null){
            hql += "AND n.notificationStatus = :status ";
        }
        hql += "ORDER BY n.dateTime DESC";
        Query query = entityManager.createQuery(hql);
        query.setParameter("username", username);
        if(status != null){
            query.setParameter("status", status);
        }
        query.setFirstResult((page - 1) * PAGE_SIZE);
        query.setMaxResults(PAGE_SIZE);
        List<Notification> notifications = query.getResultList();
        return notifications;
    }
}
