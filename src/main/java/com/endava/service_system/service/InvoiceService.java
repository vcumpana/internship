package com.endava.service_system.service;

import com.endava.service_system.dao.InvoiceEntityManagerDao;
import com.endava.service_system.dto.InvoiceDisplayDto;
import com.endava.service_system.model.InvoiceFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    public void setInvoiceEntityManagerDao(InvoiceEntityManagerDao invoiceEntityManagerDao) {
        this.invoiceEntityManagerDao = invoiceEntityManagerDao;
    }

    private InvoiceEntityManagerDao invoiceEntityManagerDao;

    public List<InvoiceDisplayDto> getAllInvoices(InvoiceFilter filter){
        return invoiceEntityManagerDao.getAllInvoices(filter);
    }
}
