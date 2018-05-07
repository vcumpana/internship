package com.endava.service_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Pattern(regexp = "^[^<'\">]+$",message = "You can't use tags and \"")
    @Email
    @NotEmpty
    @Size(min = 1,max = 255)
    private String fromEmail;
    @Pattern(regexp = "^[^<'\">]+$",message = "You can't use tags and \"")
    @Size(min = 1,max = 255)
    private String text;
    private boolean read;
    private LocalDateTime date;
}
