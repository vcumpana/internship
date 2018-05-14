package com.endava.service_system.dto;

import com.endava.service_system.constraints.DateConsecutiveConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DateConsecutiveConstraint.List({
        @DateConsecutiveConstraint(beginDate = "fromDate",
                endDate = "tillDate",
                message = "End date should be grater than begin date"),
        @DateConsecutiveConstraint(beginDate = "tillDate",
                endDate = "dueDate",
                message = "Due date should be grater than end date")}
)
public class NewInvoiceDTO {

    @NotNull
    @DecimalMax("999999999999.00")
    @DecimalMin("0.00")
    private BigDecimal price;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fromDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate tillDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @NotNull
    private Long contractId;

    private Long invoiceId;

    private String clientName;

    private String service;
}
