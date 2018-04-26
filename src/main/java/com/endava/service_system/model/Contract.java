package com.endava.service_system.model;

import com.endava.service_system.enums.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.Period;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Company company;

    @ManyToOne
    private User user;

    @OneToOne
    private Service service;

    private LocalDate date;

    private Period term;

    private ContractStatus status;
}
