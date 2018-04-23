package com.endava.service_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
}
