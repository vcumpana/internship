package com.endava.service_system.model.entities;

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
    private long id;
    @Column(unique = true)
    private String name;

    @OneToOne
    private Credential credential;

    private String address;

    private String companyUrl;

    private String imageName;

    @OneToMany
    private Collection<Contract> contracts;

    @OneToMany
    private Collection<Service> services;

    @OneToMany
    private Collection<Invoice> invoices;
}
