package com.endava.service_system.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentDto {
    private Long C;
    private BigDecimal S;
    private String D;
}
