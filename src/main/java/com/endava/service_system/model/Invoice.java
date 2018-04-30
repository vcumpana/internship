package com.endava.service_system.model;

import com.endava.service_system.enums.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal price;

    private LocalDate fromDate;

    private LocalDate tillDate;

    private LocalDate dueDate;

    @ManyToOne
    private Contract contract;

    private InvoiceStatus invoiceStatus;
}
