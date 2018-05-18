package com.endava.service_system.converter;

import com.endava.service_system.dao.CompanyDao;
import com.endava.service_system.dao.UserDao;
import com.endava.service_system.model.dto.NotificationForUserDto;
import com.endava.service_system.model.entities.Company;
import com.endava.service_system.model.entities.Notification;
import com.endava.service_system.model.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.endava.service_system.model.enums.Role.ROLE_COMPANY;
import static com.endava.service_system.model.enums.Role.ROLE_USER;

@Component
public class NotificationToNotificationForUserConverter implements Converter<Notification, NotificationForUserDto>{
    private CompanyDao companyDao;
    private UserDao userDao;

    public NotificationToNotificationForUserConverter(CompanyDao companyDao, UserDao userDao) {
        this.companyDao = companyDao;
        this.userDao = userDao;
    }

    @Override
    public NotificationForUserDto convert(Notification notification) {
        NotificationForUserDto notificationForUserDto = new NotificationForUserDto();
        notificationForUserDto.setId(notification.getId());
        if(notification.getSender().getRole() == ROLE_COMPANY) {
            Company company = companyDao.getByUsername(notification.getSender().getUsername()).get();
            notificationForUserDto.setSender(company.getName());
        } else if(notification.getSender().getRole() == ROLE_USER){
            User user = userDao.getByUsername(notification.getSender().getUsername()).get();
            notificationForUserDto.setSender(user.getName() + " " + user.getSurname());
        } else {
            notificationForUserDto.setSender("Payment System");
        }
        notificationForUserDto.setMessage(notification.getMessage());
        notificationForUserDto.setStatus(notification.getNotificationStatus());
        notificationForUserDto.setDateAndTime(convertLocalDateTimeToString(notification.getDateTime()));
        return notificationForUserDto;
    }

    private String convertLocalDateTimeToString(LocalDateTime localDateTime){
        String time = "";
        time += localDateTime.getDayOfWeek() + ", " + localDateTime.getDayOfMonth() + " " + localDateTime.getMonth() + " " + localDateTime.getYear();
        time += "  ";
        time += localDateTime.getHour() + ":" + localDateTime.getMinute();
        return time;
    }
}
