package com.endava.service_system.converter;

import com.endava.service_system.model.dto.NewInvoiceDTO;
import com.endava.service_system.model.entities.Invoice;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.endava.service_system.model.enums.InvoiceStatus.CREATED;

@Component
public class NewInvoiceDTOConverter implements Converter<NewInvoiceDTO, Invoice> {

    @Override
    public Invoice convert(NewInvoiceDTO newInvoiceDTO) {
        Invoice invoice = new Invoice();

        invoice.setFromDate(newInvoiceDTO.getFromDate());
        invoice.setTillDate(newInvoiceDTO.getTillDate());
        invoice.setDueDate(newInvoiceDTO.getDueDate());
        invoice.setPrice(newInvoiceDTO.getPrice());
        invoice.setInvoiceStatus(CREATED);
        if (newInvoiceDTO.getInvoiceId() != null)
            invoice.setId(newInvoiceDTO.getInvoiceId());
        return invoice;
    }

}

