package com.endava.service_system.model.dto;

import lombok.Data;

@Data
public class PaymentDto {
    private Long CorrespondentCount;
    private double Sum;
    private String Description;
}
