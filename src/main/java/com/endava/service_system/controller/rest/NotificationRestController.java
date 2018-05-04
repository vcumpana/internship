package com.endava.service_system.controller.rest;

import com.endava.service_system.dto.NotificationForUserDto;
import com.endava.service_system.model.Notification;
import com.endava.service_system.service.NotificationService;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NotificationRestController {
    private NotificationService notificationService;

    public NotificationRestController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping(value = "notification/currentUser")
    public List<NotificationForUserDto> getAllNotificationsForUser(@RequestParam("page") int page){
        return notificationService.getAllNotificationsForUser(page);
    }

    @GetMapping(value = "notification/getNumberOfUnread")
    public int getNumberOfUnreadNotifications(){
        return notificationService.getCountOfUnreadNotifications();
    }

    @GetMapping(value = "notification/markAsRead")
    public void markNotificationAsRead(@RequestParam("notificationId") long id){
        notificationService.markNotificationAsRead(id);
    }
}
