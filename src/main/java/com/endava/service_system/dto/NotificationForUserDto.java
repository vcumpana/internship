package com.endava.service_system.dto;

import com.endava.service_system.enums.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationForUserDto {
    long id;
    String sender;
    String message;
    NotificationStatus status;
    String dateAndTime;
}
