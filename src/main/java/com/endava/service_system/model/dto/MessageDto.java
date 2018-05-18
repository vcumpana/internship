package com.endava.service_system.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class MessageDto {
    @Pattern(regexp = "^[^<'\">]+$",message = "You can't use tags and \"")
    @Email
    @NotEmpty
    @Size(min = 1,max = 255)
    private String fromEmail;
    @Pattern(regexp = "^[^<'\">]+$",message = "You can't use tags and \"")
    @Size(min = 1,max = 255)
    private String text;
}
