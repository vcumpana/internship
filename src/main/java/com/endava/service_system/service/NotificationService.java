package com.endava.service_system.service;

import com.endava.service_system.constraints.FieldsValueMatch;
import com.endava.service_system.dao.*;
import com.endava.service_system.dto.ContractDtoFromUser;
import com.endava.service_system.dto.NotificationForUserDto;
import com.endava.service_system.enums.ContractStatus;
import com.endava.service_system.model.Company;
import com.endava.service_system.model.Contract;
import com.endava.service_system.model.Notification;
import com.endava.service_system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.endava.service_system.enums.NotificationStatus.READ;
import static com.endava.service_system.enums.NotificationStatus.UNREAD;

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
}
