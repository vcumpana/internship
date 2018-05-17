package com.endava.service_system.model.dto;

import com.endava.service_system.model.enums.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractForShowingDto {

    private long id;

    private String serviceTitle;

    private String categoryName;

    private String companyName;

    private String fullName;

    private String startDate;

    private String endDate;

    private BigDecimal servicePrice;

    private ContractStatus contractStatus;
}
