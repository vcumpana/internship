package com.endava.service_system.model.entities;

import com.endava.service_system.model.enums.Role;
import com.endava.service_system.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "credentials")
public class Credential {
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private long id;
    @Column(unique = true)
    private String username;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToOne
    private BankAccount bankAccount;
}
