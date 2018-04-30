package com.endava.service_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewInvoiceDTO {

    private BigDecimal price;

    private LocalDate fromDate;

    private LocalDate tillDate;

    private LocalDate dueDate;

    private Long contractId;
}
