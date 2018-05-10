package com.endava.service_system.dto;

import com.endava.service_system.enums.InvoiceStatus;
import com.endava.service_system.model.Credential;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceForPaymentDto {
    private long invoiceId;
    private double price;
    private Long companyBankCount;
    private String userUsername;
    private InvoiceStatus status;
    private String fullName;
    private Credential userCredential;
    private Credential companyCredential;
    private String serviceTitle;
    public InvoiceForPaymentDto(long invoiceId, BigDecimal price,Long companyBankCount,
                                InvoiceStatus status,String fullName, Credential userCredential,
                                Credential companyCredential,String serviceTitle){
        setInvoiceId(invoiceId);
        setPrice(price.doubleValue());
        setCompanyBankCount(companyBankCount);
        setUserUsername(userCredential.getUsername());
        setStatus(status);
        setFullName(fullName);
        setUserCredential(userCredential);
        setCompanyCredential(companyCredential);
        setServiceTitle(serviceTitle);
    }
}
