package com.endava.service_system.dto;

import com.endava.service_system.enums.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractToUserDto {

    private long id;

    private String serviceTitle;

    private String categoryName;

    private String companyName;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal servicePrice;

    private ContractStatus contractStatus;
}
