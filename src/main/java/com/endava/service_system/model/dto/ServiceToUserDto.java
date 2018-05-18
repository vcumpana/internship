package com.endava.service_system.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceToUserDto {

    private long id;

    private String title;

    private String category;

    private String companyName;

    private String description;

    private BigDecimal price;

    private String imageName;

    private String companyUrl;
}
