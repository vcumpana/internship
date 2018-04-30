package com.endava.service_system.dto;

import com.endava.service_system.enums.InvoiceStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class InvoiceDisplayDto {
    private String companyTitle;
    private String userTitle;
    private long invoiceId;
    private BigDecimal price;
    private InvoiceStatus invoiceStatus;
    private String serviceTitle;
    private LocalDate paymentDate;
    private LocalDate fromDate;
    private LocalDate tillDate;
}
