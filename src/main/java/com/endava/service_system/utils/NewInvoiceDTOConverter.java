package com.endava.service_system.utils;

import com.endava.service_system.dto.NewInvoiceDTO;
import com.endava.service_system.dto.NewServiceDTO;
import com.endava.service_system.enums.InvoiceStatus;
import com.endava.service_system.model.Contract;
import com.endava.service_system.model.Invoice;
import com.endava.service_system.model.Service;
import com.endava.service_system.service.CategoryService;
import com.endava.service_system.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.endava.service_system.enums.InvoiceStatus.SENT;

@Component
public class NewInvoiceDTOConverter implements Converter<NewInvoiceDTO, Invoice> {

    private CategoryService categoryService;

    @Override
    public Invoice convert(NewInvoiceDTO newInvoiceDTO) {
        Invoice invoice = new Invoice();

        invoice.setFromDate(newInvoiceDTO.getFromDate());
        invoice.setTillDate(newInvoiceDTO.getTillDate());
        invoice.setDueDate(newInvoiceDTO.getDueDate());
        invoice.setPrice(newInvoiceDTO.getPrice());
        invoice.setInvoiceStatus(SENT);

        return invoice;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

}

