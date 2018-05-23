package com.endava.service_system.model.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "bank_keys")
public class BankKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //@Lob
    private byte[] bankModulus;

    //@Lob
    private byte[] javaPrivateKey;

}
