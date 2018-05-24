package com.endava.service_system.model.dto;

import com.endava.service_system.model.enums.NotificationStatus;
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
