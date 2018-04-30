package com.endava.service_system.service;

import com.endava.service_system.dao.InvoiceDao;
import com.endava.service_system.model.Invoice;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    private InvoiceDao invoiceDao;

    public InvoiceService(InvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
    }

    public Invoice save(Invoice invoice) {
        return invoiceDao.save(invoice);
    }
}
