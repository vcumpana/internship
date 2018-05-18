package com.endava.service_system.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class StatementRangeDto {
    private LocalDate Date;
    private LocalDate DateTo;
}
