package com.endava.service_system.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class NormalTransaction {
    private String date;
    private Long mainCount;
    private Long correspondentCount;
    private BigDecimal sum;
    private String description;
}
