package com.endava.service_system.utils;

import com.endava.service_system.dto.InvoiceDisplayDto;
import com.endava.service_system.enums.InvoiceStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class ObjectArrayToInvoiceDisplayCoverter implements Converter<Object[],InvoiceDisplayDto> {
    /**
     * user.name+' '+user.surname,company.name,invoice.id," +
     " invoice.price,invoice.invoiceStatus,service.title," +
     " invoice.dueDate,invoice.fromDate,invoice.tillDate, contract.id" +
     */
    @Override
    public InvoiceDisplayDto convert(Object[] source) {
        return InvoiceDisplayDto.builder()
                .userTitle((String) source[0])
                .companyTitle((String) source[1])
                .invoiceId((Long) source[2])
                .price((BigDecimal) source[3])
                .invoiceStatus((InvoiceStatus) source[4])
                .serviceTitle((String) source[5])
                .paymentDate((LocalDate) source[6])
                .fromDate((LocalDate) source[7])
                .tillDate((LocalDate) source[8])
                .contractId((Long) source[9])
                .build();
    }
}
