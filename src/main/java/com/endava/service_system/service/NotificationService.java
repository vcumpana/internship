package com.endava.service_system.service;

import com.endava.service_system.dao.*;
import com.endava.service_system.model.dto.ContractDtoFromUser;
import com.endava.service_system.model.dto.NotificationForUserDto;
import com.endava.service_system.model.entities.*;
import com.endava.service_system.model.enums.ContractStatus;
import com.endava.service_system.model.enums.NotificationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.endava.service_system.model.enums.NotificationStatus.READ;
import static com.endava.service_system.model.enums.NotificationStatus.UNREAD;

@Service
public class NotificationService {
    private NotificationDao notificationDao;
    private NotificationEntityMangerDao notificationEntityMangerDao;
    private UserDao userDao;
    private CompanyDao companyDao;
    private ICredentialDao credentialDao;
    private ConversionService conversionService;

    @Autowired
    public void setNotificationDao(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

    @Autowired
    public void setNotificationEntityMangerDao(NotificationEntityMangerDao notificationEntityMangerDao) {
        this.notificationEntityMangerDao = notificationEntityMangerDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setCompanyDao(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    @Autowired
    public void setCredentialDao(ICredentialDao credentialDao) {
        this.credentialDao = credentialDao;
    }

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public void saveAboutContractFromUser(ContractDtoFromUser contractDtoFromUser){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Company company = companyDao.getByName(contractDtoFromUser.getCompanyName()).get();
        User user = userDao.getByUsername(username).get();
        Notification notification = new Notification();
        notification.setSender(credentialDao.getByUsername(username).get());
        notification.setRecipient(credentialDao.getByCompanyName(company.getName()).get());
        notification.setMessage("Customer " + user.getSurname() + " " + user.getSurname() + " wants to sign with your company contract.");
        notification.setNotificationStatus(UNREAD);
        notification.setDateTime(LocalDateTime.now());
        notificationDao.save(notification);
    }

    public void saveAboutContractFromCompany(Contract contract){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Company company = companyDao.getByUsername(username).get();
        User user = contract.getUser();
        Notification notification = new Notification();
        notification.setSender(credentialDao.getByUsername(username).get());
        notification.setRecipient(credentialDao.getByUserId(user.getId()).get());
        notification.setMessage("Company " + company.getName() + (contract.getStatus() == ContractStatus.ACTIVE ? " approved" : " denied") + " your request to sign contract.");
        notification.setNotificationStatus(UNREAD);
        notification.setDateTime(LocalDateTime.now());
        notificationDao.save(notification);
    }

    public void saveNotifications(List<Notification> notifications){
        notificationDao.saveAll(notifications);
    }

    private Notification createNotificationForCredential(Credential from, Credential to, LocalDateTime now, String message){
        Notification notification=new Notification();
        notification.setNotificationStatus(NotificationStatus.UNREAD);
        notification.setDateTime(now);
        notification.setMessage(message);
        notification.setSender(from);
        notification.setRecipient(to);
        notification.setMessage(message);
        return notification;
    }

    public Notification createNotificationPaymentOverduedForCompany(User user, Company company, Credential adminCredential, Contract contract, LocalDateTime now){
        String message=user.getName()+" "+user.getSurname()+" has overdued payment for "+contract.getService().getTitle()+" .Contract Nr "+contract.getId()+" ";
        Notification notification=createNotificationForCredential(adminCredential,company.getCredential(),now,message);
        return notification;
    }

    public Notification createNotificationPaymentOverduedForUser(User user, Company company, Credential adminCredential, Contract contract, LocalDateTime now){
        String message="You have overdued payment to "+company.getName()+" for "+contract.getService().getTitle()+" .Contract Nr "+contract.getId();
        Notification notification=createNotificationForCredential(adminCredential,user.getCredential(),now,message);
        return notification;
    }

    public Notification createNotificationPayedForCompany(String userFullName,Long invoiceId,String serviceTitle,Credential adminCredential,Credential companyCredential,LocalDateTime now){
        String message="User "+userFullName+
                " has payed for invoice : "+invoiceId+
                " ,for service : "+serviceTitle+".";
        Notification notification=createNotificationForCredential(adminCredential,companyCredential,now,message);
        return notification;
    }

    public Notification createNotificationPayedForUser(Long invoiceId,String serviceTitle,Credential adminCredential,Credential userCredential,LocalDateTime now){
        String message="Succesful payment of invoice : "+invoiceId+" , service "+serviceTitle+".";
        Notification notification=createNotificationForCredential(adminCredential,userCredential,now,message);
        return notification;
    }

    public List<NotificationForUserDto> getAllNotificationsForUser(int page){
        List<Notification> notifications = notificationEntityMangerDao.getAllNotificationsForUser(SecurityContextHolder.getContext().getAuthentication().getName(), page);
        List<NotificationForUserDto> notificationForUserDto = new ArrayList();
        for(Notification n: notifications){
            notificationForUserDto.add(conversionService.convert(n, NotificationForUserDto.class));
        }
        return notificationForUserDto;
    }

    public int getCountOfUnreadNotifications(){
        return notificationDao.getListOfUnreadNotification(SecurityContextHolder.getContext().getAuthentication().getName()).size();
    }

    public void markNotificationAsRead(long id){
        Notification notification = notificationDao.getOne(id);
        if(notification.getRecipient().getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName())){
            notification.setNotificationStatus(READ);
        }
        notificationDao.save(notification);
    }

    public void saveAboutInvoiceFromCompany(Invoice invoice){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Company company = invoice.getContract().getCompany();
        User user =invoice.getContract().getUser();
        Notification notification = new Notification();
        notification.setRecipient(user.getCredential());
        notification.setSender(company.getCredential());
        notification.setMessage("Company " + company.getName() + " has sent an invoice to pay according to contract number "
                                + invoice.getContract().getId() + " from " + invoice.getContract().getStartDate());
        notification.setNotificationStatus(UNREAD);
        notification.setDateTime(LocalDateTime.now());
        notificationDao.save(notification);
    }
}
