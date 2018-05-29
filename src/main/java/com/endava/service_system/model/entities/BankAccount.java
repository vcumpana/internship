package com.endava.service_system.model.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long countNumber;
    private Long accessKey;
}
