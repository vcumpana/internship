package com.endava.service_system.model.entities;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Statement {
    private LocalDateTime date;
    private Long mainCount;
    private Long correspondentCount;
    private double sum;
    private String description;

}
