package com.endava.service_system.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ShortTransactionsDto {
    private long p;
    private BigDecimal bfc;
    private BigDecimal bf;
    private BigDecimal ba;
    private List<ShortTransaction> listOfTransactions;
}
