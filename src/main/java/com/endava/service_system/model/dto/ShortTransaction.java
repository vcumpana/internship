package com.endava.service_system.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShortTransaction {
    private String d;
    private Long m;
    private Long c;
    private BigDecimal s;
    private String dr;
}
