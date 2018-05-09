package com.endava.service_system.dto;

import com.endava.service_system.enums.InvoiceStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceForPaymentDto {
    private long id;
    private double price;
    private Long companyBankCount;
    private String userUsername;
    private InvoiceStatus status;
    public InvoiceForPaymentDto(long id, BigDecimal price,Long companyBankCount,String username,InvoiceStatus status){
        setId(id);
        setPrice(price.doubleValue());
        setCompanyBankCount(companyBankCount);
        setUserUsername(username);
        setStatus(status);
    }
}
