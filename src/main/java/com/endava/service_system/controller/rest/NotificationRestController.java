package com.endava.service_system.controller.rest;

import com.endava.service_system.model.dto.NotificationForUserDto;
import com.endava.service_system.model.enums.NotificationStatus;
import com.endava.service_system.service.NotificationService;
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
    public List<NotificationForUserDto> getAllNotificationsForUser(@RequestParam("page") int page,
                                                                   @RequestParam(value="status", required = false) NotificationStatus status){
        return notificationService.getAllNotificationsForUser(page, status);
    }

    @GetMapping(value = "notification/getNumberOfUnread")
    public int getNumberOfUnreadNotifications(){
        return notificationService.getCountOfUnreadNotifications();
    }

    @GetMapping(value = "notification/markAsRead")
    public void markNotificationAsRead(@RequestParam("notificationId") long id){
        notificationService.markNotificationAsRead(id);
    }

    @GetMapping(value = "notification/markAllAsRead")
    public void markAllNotificationAsRead(){
        notificationService.markAllNotificationsAsRead();
    }

}
