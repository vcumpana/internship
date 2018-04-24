package com.endava.service_system.model;

import com.endava.service_system.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToOne
    private Credential credential;

    private String bankAccount;

    private String address;

    private String email;

    @OneToMany
    private Collection<Contract> contracts;

    @ManyToMany
    private Collection<Service> services;

    @OneToMany
    private Collection<Invoice> invoices;
}
