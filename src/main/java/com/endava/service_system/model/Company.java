package com.endava.service_system.model;

import com.endava.service_system.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Credential credential;

    private String bankAccount;

    private String address;

    private String email;

    @OneToMany
    private Collection<Contract> contracts;

    @OneToMany
    private Collection<Service> services;

    @OneToMany
    private Collection<Invoice> invoices;
}
