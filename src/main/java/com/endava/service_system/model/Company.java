package com.endava.service_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@AllArgsConstructor
@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private String name;

    private String username;

    private String password;

    private String bankAccount;

    private String address;

    @OneToMany
    private Collection<Contract> contracts;

    @ManyToMany
    private Collection<Service> services;

    @OneToMany
    private Collection<Invoice> invoices;




}
