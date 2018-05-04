package com.endava.service_system.utils;

import com.endava.service_system.dao.CompanyDao;
import com.endava.service_system.dto.NotificationForUserDto;
import com.endava.service_system.model.Company;
import com.endava.service_system.model.Notification;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificationToNotificationForUserConverter implements Converter<Notification, NotificationForUserDto>{
    private CompanyDao companyDao;

    public NotificationToNotificationForUserConverter(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    @Override
    public NotificationForUserDto convert(Notification notification) {
        NotificationForUserDto notificationForUserDto = new NotificationForUserDto();
        notificationForUserDto.setId(notification.getId());
        Company company = companyDao.getByUsername(notification.getSender().getUsername()).get();
        notificationForUserDto.setSender(company.getName());
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
