package com.endava.service_system.service;

import com.endava.service_system.dao.InvoiceEntityManagerDao;
import com.endava.service_system.dto.InvoiceDisplayDto;
import com.endava.service_system.model.InvoiceFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.endava.service_system.dao.InvoiceDao;
import com.endava.service_system.model.Invoice;

import java.util.List;

@Service
public class InvoiceService {
    private InvoiceEntityManagerDao invoiceEntityManagerDao;
    private InvoiceDao invoiceDao;

    public List<InvoiceDisplayDto> getAllInvoices(InvoiceFilter filter) {
        return invoiceEntityManagerDao.getAllInvoices(filter);
    }

    public Invoice save(Invoice invoice) {
        return invoiceDao.save(invoice);
    }

    @Autowired
    public void setInvoiceEntityManagerDao(InvoiceEntityManagerDao invoiceEntityManagerDao) {
        this.invoiceEntityManagerDao = invoiceEntityManagerDao;
    }

    @Autowired
    public void setInvoiceDao(InvoiceDao invoiceDao){
        this.invoiceDao=invoiceDao;
    }
}