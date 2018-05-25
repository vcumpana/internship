package com.endava.service_system.utils;

import com.endava.service_system.model.entities.*;
import com.endava.service_system.service.CredentialService;
import com.endava.service_system.service.InvoiceService;
import com.endava.service_system.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {
    private final static long ONCE_AN_HOUR =60*60*1000;
    private final static int AMOUNT_OF_NOTIFICATIONS_AT_ONCE=20;
    private final static Logger LOGGER= LogManager.getLogger(ScheduledTasks.class);
    private final InvoiceService invoiceService;
    private final CredentialService credentialService;
    private final NotificationService notificationService;
    @Scheduled(fixedRate = ONCE_AN_HOUR)
    public void changeStatusOfInvoices() {
        LOGGER.log(Level.DEBUG,"start changing invoice");
        LocalDate today=getCurrentDate();
        invoiceService.deleteForgotenInvoices(today);
        changeStatusOfInvoices(today);
        LOGGER.log(Level.DEBUG,"end of changing invoice");
    }

    private void changeStatusOfInvoices(LocalDate today){
        List<Invoice> invoices=invoiceService.getSentInvoicesThatHaveDueDateBefore(today,AMOUNT_OF_NOTIFICATIONS_AT_ONCE);
        Credential admin=getAdminCredentials();
        List<Notification> notifications=new ArrayList<>();
        List<Long> invoceIds=new ArrayList<>();
        while (invoices.size()!=0){
            for(Invoice invoice:invoices){
                LOGGER.log(Level.DEBUG,invoice.getContract().getCompany().getCredential());
                LOGGER.log(Level.DEBUG,invoice.getContract().getUser().getCredential());
                User user=invoice.getContract().getUser();
                Company company=invoice.getContract().getCompany();
                Contract contract=invoice.getContract();
                Notification userNotification=notificationService.createNotificationPaymentOverduedForCompany(user,company,admin,contract,LocalDateTime.now());
                Notification companyNotification=notificationService.createNotificationPaymentOverduedForUser(user,company,admin,contract,LocalDateTime.now());
                notifications.add(userNotification);
                notifications.add(companyNotification);
                invoceIds.add(invoice.getId());
                LOGGER.debug("cycle has end");
            }
            notificationService.saveNotifications(notifications);
            invoiceService.makeInvoicesOverdued(invoceIds);
            notifications.clear();
            invoceIds.clear();
            invoices=invoiceService.getSentInvoicesThatHaveDueDateBefore(today,AMOUNT_OF_NOTIFICATIONS_AT_ONCE);
        }
    }


    private LocalDate getCurrentDate(){
        return LocalDate.now();
    }

    private Credential getAdminCredentials(){
        return credentialService.getDefaultAdminCredential();
    }
}
