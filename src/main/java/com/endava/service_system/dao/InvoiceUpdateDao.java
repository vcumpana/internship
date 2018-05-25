package com.endava.service_system.dao;

import com.endava.service_system.model.entities.Invoice;
import com.endava.service_system.model.enums.InvoiceStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

@Repository
public class InvoiceUpdateDao {
    private static final int INVOICE_DEFAULT_LIMIT=20;
    private static final String HQL_FOR_OVERDUED_INVOICES="SELECT inv FROM Invoice inv " +
            "JOIN FETCH inv.contract contr " +
            "JOIN FETCH contr.company comp " +
            "JOIN FETCH comp.credential compCredentials "+
            "JOIN FETCH contr.user us " +
            "JOIN FETCH contr.service serv "+
            "JOIN FETCH us.credential userCredentials "+
            "WHERE inv.invoiceStatus=:invoiceStatus " +
            "and inv.dueDate<:today";
    private static final String HQL_FOR_DELETING_FORGOTEN_INVOICES="DELETE FROM Invoice inv WHERE inv.invoiceStatus=:invoiceStatus and inv.dueDate<:today ";
    @PersistenceContext
    private EntityManager manager;

    public List<Invoice> getSentInvoicesThatHaveDueDateBefore(LocalDate currentDate,Integer limit){
        if(limit==null || limit<1){
            limit=INVOICE_DEFAULT_LIMIT;
        }
        Query query=manager.createQuery(HQL_FOR_OVERDUED_INVOICES);
        query.setParameter("today",currentDate);
        query.setParameter("invoiceStatus", InvoiceStatus.SENT);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Transactional
    public void deleteForgotenInvoices(LocalDate currentDate){
        Query query=manager.createQuery(HQL_FOR_DELETING_FORGOTEN_INVOICES);
        query.setParameter("today",currentDate);
        query.setParameter("invoiceStatus", InvoiceStatus.CREATED);
        query.executeUpdate();
    }


}
