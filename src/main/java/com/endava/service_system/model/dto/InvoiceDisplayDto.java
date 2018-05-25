package com.endava.service_system.model.dto;

import com.endava.service_system.model.enums.InvoiceStatus;
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
    private long contractId;
    private BigDecimal price;
    private InvoiceStatus invoiceStatus;
    private String serviceTitle;
    private String paymentDate;
    private String fromDate;
    private String tillDate;
    private String createdDate;
}
