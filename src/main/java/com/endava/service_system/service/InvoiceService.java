package com.endava.service_system.service;

import com.endava.service_system.dao.CurrentDateDao;
import com.endava.service_system.dao.InvoiceEntityManagerDao;
import com.endava.service_system.dao.InvoiceUpdateDao;
import com.endava.service_system.dto.InvoiceDisplayDto;
import com.endava.service_system.dto.InvoiceForPaymentDto;
import com.endava.service_system.model.*;
import com.endava.service_system.enums.ContractStatus;
import com.endava.service_system.enums.InvoiceStatus;
import com.endava.service_system.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.endava.service_system.dao.InvoiceDao;
import com.endava.service_system.model.Invoice;
import org.springframework.transaction.annotation.Transactional;
import sun.rmi.runtime.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.endava.service_system.enums.ContractStatus.ACTIVE;
import static com.endava.service_system.enums.InvoiceStatus.CREATED;
import static com.endava.service_system.enums.InvoiceStatus.PAID;
import static com.endava.service_system.enums.InvoiceStatus.SENT;

@Service
public class InvoiceService {
    private InvoiceEntityManagerDao invoiceEntityManagerDao;
    private InvoiceDao invoiceDao;
    private ContractService contractService;
    private CurrentDateDao currentDateDao;
    private NotificationService notificationService;

    @Autowired
    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    private InvoiceUpdateDao invoiceUpdateDao;

    @Autowired
    public void setInvoiceUpdateDao(InvoiceUpdateDao invoiceUpdateDao) {
        this.invoiceUpdateDao = invoiceUpdateDao;
    }

    @Autowired
    public void setCurrentDateDao(CurrentDateDao currentDateDao) {
        this.currentDateDao = currentDateDao;
    }
    @Autowired
    public void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }

    @Autowired
    public void setInvoiceEntityManagerDao(InvoiceEntityManagerDao invoiceEntityManagerDao) {
        this.invoiceEntityManagerDao = invoiceEntityManagerDao;
    }

    @Autowired
    public void setInvoiceDao(InvoiceDao invoiceDao){
        this.invoiceDao=invoiceDao;
    }

    public List<InvoiceDisplayDto> getAllInvoices(InvoiceFilter filter) {
        return invoiceEntityManagerDao.getAllInvoices(filter);
    }

    public Invoice save(Invoice invoice) {
        return invoiceDao.save(invoice);
    }

    public Long getInvoicePagesNr(InvoiceFilter filter) {
        return invoiceEntityManagerDao.getPagesSizeForFilter(filter);
    }

    public void createInvoicesFromBulk(List<String> ids) {
        Contract contract;
        Invoice invoice;
        for (int i = 0; i < ids.size(); i++) {
            contract = contractService.getContractById(Integer.parseInt(ids.get(i)));
            if (contract.getStatus() != ACTIVE)
                continue;
            invoice = new Invoice();
            invoice.setContract(contract);
            invoice.setInvoiceStatus(CREATED);
            Company company = contract.getCompany();
            List<Invoice> invoices = contract.getInvoices();
            if (invoices != null && invoices.size() > 0) {
                if (invoices.get(invoices.size() - 1).getTillDate().plusDays(1).isAfter(currentDateDao.findById(new Long(1)).get().getLocalDate()) ||
                        invoices.get(invoices.size() - 1).getTillDate().plusDays(1).isAfter(contract.getEndDate()))
                    continue;
                invoice.setFromDate(invoices.get(invoices.size() - 1).getTillDate().plusDays(1));
            } else
                invoice.setFromDate(contract.getStartDate());
            if (contract.getEndDate().isBefore(currentDateDao.findById(new Long(1)).get().getLocalDate()))
                invoice.setTillDate(contract.getEndDate());
            else
                invoice.setTillDate(currentDateDao.findById(new Long(1)).get().getLocalDate());
            invoice.setDueDate(invoice.getTillDate().plusDays(10));
            invoice.setPrice(contract.getService().getPrice().multiply(new BigDecimal(getPeriodBetweenDates(invoice.getFromDate(),invoice.getTillDate()).getMonths()))
                    .add(contract.getService().getPrice()
                            .multiply(new BigDecimal(getPeriodBetweenDates(invoice.getFromDate(), invoice.getTillDate()).getDays() + 1))
                            .divide(new BigDecimal(invoice.getFromDate().lengthOfMonth()), 2, RoundingMode.HALF_UP)));
            invoiceDao.save(invoice);
        }
    }

    public Period getPeriodBetweenDates(LocalDate firstInputDate, LocalDate secondInputDate) {
        final Period period = Period.between(firstInputDate, secondInputDate);
        System.out.println("days:"+period.getDays());
        return period;
    }

    public void sendInvoicesFromBulk(List<String> ids) {
        Invoice invoice;
        for (int i = 0;i < ids.size(); i++){
            invoice = invoiceDao.getOne(Long.parseLong(ids.get(i)));
            if (invoice.getInvoiceStatus() == CREATED){
                invoice.setInvoiceStatus(SENT);
                invoiceDao.save(invoice);
                notificationService.saveAboutInvoiceFromCompany(invoice);
            }
        }
    }

    public void cancelInvoicesFromBulk(List<String> ids) {
        Invoice invoice;
        for (int i = 0;i < ids.size(); i++){
            invoice = invoiceDao.getOne(Long.parseLong(ids.get(i)));
            if (invoice.getInvoiceStatus() == CREATED){
                invoiceDao.delete(invoice);
            }
        }
    }

    public void deleteInvoice(Invoice invoice) {
        invoiceDao.delete(invoice);
    }

    public Invoice getInvoiceById(Long invoiceId) {
        return invoiceDao.getOne(invoiceId);
    }

    public void update(Invoice invoice) {
        invoiceDao.save(invoice);
    }

    public List<Invoice> getSentInvoicesThatHaveDueDateBefore(LocalDate currentDate,Integer limit){
        return invoiceUpdateDao.getSentInvoicesThatHaveDueDateBefore(currentDate,limit);
    }

    public void deleteForgotenInvoices(LocalDate currentDate){
        invoiceUpdateDao.deleteForgotenInvoices(currentDate);
    }

    @Transactional
    public void makeInvoicesOverdued(List<Long> invoceIds) {
        invoiceDao.setStatus(InvoiceStatus.OVERDUE,invoceIds);
    }

    @Transactional
    public void makeInvoicesPayed(List<Long> invoceIds){
        invoiceDao.setStatus(InvoiceStatus.PAID,invoceIds);
    }

    @Transactional
    public void makeInvoicePayed(Long id){
        List ids=new ArrayList();
        ids.add(id);
        makeInvoicesPayed(ids);
    }

    public Optional<InvoiceForPaymentDto> getFullInvoiceById(int id) {
        return invoiceDao.getFullInvoiceById(id);
    }

}