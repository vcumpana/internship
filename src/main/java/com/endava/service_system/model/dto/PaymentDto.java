package com.endava.service_system.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentDto {
    private Long correspondentCount;
    private BigDecimal sum;
    private String description;
}
