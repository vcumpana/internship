package com.endava.service_system.model.entities;

import com.endava.service_system.model.enums.InvoiceStatus;
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
    private long id;

    private BigDecimal price;

    private LocalDate fromDate;

    private LocalDate tillDate;

    private LocalDate dueDate;

    private LocalDate createdDate;

    @ManyToOne(optional=false)
    @JoinColumn(name="contract_id", referencedColumnName="id")
    private Contract contract;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus invoiceStatus;
}
