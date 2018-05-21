package com.endava.service_system.model.dto;

import com.endava.service_system.model.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceToCompanyDto {

    private long id;

    private String title;

    private Category category;

    private String description;

    private BigDecimal price;

    private int numberOfContracts;
}
