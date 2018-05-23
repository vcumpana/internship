package com.endava.service_system.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ShortTransactionsDto {
    private long pages;
    private BigDecimal balanceBefore;
    private List<ShortTransaction> listOfTransactions;
}
