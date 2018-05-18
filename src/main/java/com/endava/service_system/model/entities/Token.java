package com.endava.service_system.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String token;
    private boolean used=false;
    private String username;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
