package com.endava.service_system.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class NormalTransactionsDto {
    private long pages;
    private BigDecimal balanceBefore;
    private List<NormalTransaction> listOfTransactions;
}
