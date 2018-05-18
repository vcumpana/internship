package com.endava.service_system.model.entities;

import com.endava.service_system.model.enums.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Company company;

    @ManyToOne
    private User user;

    @OneToOne
    private Service service;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private ContractStatus status;

    @OneToMany(cascade=CascadeType.ALL,mappedBy="contract")
    private List<Invoice> invoices;
}
