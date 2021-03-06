package com.endava.service_system.controller.rest;

import com.endava.service_system.dao.InvoiceDao;
import com.endava.service_system.model.dto.*;
import com.endava.service_system.model.entities.BankAccount;
import com.endava.service_system.model.entities.Credential;
import com.endava.service_system.model.entities.Invoice;
import com.endava.service_system.model.entities.Notification;
import com.endava.service_system.model.enums.InvoiceStatus;
import com.endava.service_system.service.BankService;
import com.endava.service_system.service.CredentialService;
import com.endava.service_system.service.InvoiceService;
import com.endava.service_system.service.NotificationService;
import com.endava.service_system.utils.AuthUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.endava.service_system.model.enums.InvoiceStatus.PAID;
import static com.endava.service_system.model.enums.Role.ROLE_COMPANY;
import static com.endava.service_system.model.enums.Role.ROLE_USER;

@RestController
@RequiredArgsConstructor
public class BankRestController {
    private static final Logger LOGGER = LogManager.getLogger(BankRestController.class);
    private RestTemplate restTemplate;
    private BankService bankService;
    private @Qualifier("bankApi")
    String bankApi;
    private CredentialService credentialService;
    private AuthUtils authUtils;
    private ObjectMapper objectMapper;
    private InvoiceService invoiceService;
    private NotificationService notificationService;
    private InvoiceDao invoiceDao;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public void setBankApi(@Qualifier("bankApi") String bankApi) {
        this.bankApi = bankApi;
    }

    @Autowired
    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }

    @Autowired
    public void setCredentialService(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @Autowired
    public void setAuthUtils(AuthUtils authUtils) {
        this.authUtils = authUtils;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Autowired
    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @Autowired
    public void setInvoiceDao(InvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
    }

    //TODO add this paths to spring security;
    @PostMapping("/bank/addmoney")
    public ResponseEntity addMoneyToBankAccount(@RequestBody Map<String, Object> data) {
        Number sum = (Number) data.get("sum");
        if (sum.doubleValue() < 0) {
            String json = "{\"message\":\"Sum should be bigger than 0 \"}";
            return new ResponseEntity(json, HttpStatus.BAD_REQUEST);
        }
        String username = authUtils.getAuthenticatedUsername();
        LOGGER.debug("user : " + username + " tries to add money to his account");
        Optional<BankAccount> bankAccountOptional = bankService.getBankAccountByUsername(username);
        if (!bankAccountOptional.isPresent()) {
            String json = "{\"message\":\"You don't have a bank account please contact admin\"}";
            return new ResponseEntity(json, HttpStatus.BAD_REQUEST);
        }
        BankAccount bankAccount = bankAccountOptional.get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("AccessKey", String.valueOf(bankAccount.getAccessKey()));
        headers.add("CountNumber", String.valueOf(bankAccount.getCountNumber()));
        //map.add("email", "first.last@example.com");
        AddMoneyDto addMoneyDto = new AddMoneyDto();
        addMoneyDto.setSum(sum.doubleValue());
        addMoneyDto.setDescription("adding money to account ");
        HttpEntity<AddMoneyDto> request = new HttpEntity<AddMoneyDto>(addMoneyDto, headers);
        ResponseEntity rs = restTemplate.postForEntity(bankApi + "bankaccount/addmoney", request, String.class);
        return new ResponseEntity(rs.getBody(), rs.getStatusCode());
    }

    @PostMapping("/bank/addbankaccount")
    public ResponseEntity addBankAccount(Authentication authentication) {

        Optional<Credential> credentialOptional = credentialService.getByUsername(authentication.getName());
        if (!credentialOptional.isPresent()) {
            String json = "{\"message\":\"Bank Problem\"}";
            return new ResponseEntity(json, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        bankService.addBankAccount(credentialOptional.get());
        return new ResponseEntity(HttpStatus.OK);
    }

    ;

    @PostMapping("/bank/statements")
    public ResponseEntity<Object> getStatements(@RequestBody Map<String, Object> data) throws JsonProcessingException {

        String username = authUtils.getAuthenticatedUsername();
        LOGGER.debug("user : " + username + " tries to see his statements");
        LOGGER.debug("range : " + data);
        Optional<BankAccount> bankAccountOptional = bankService.getBankAccountByUsername(username);
        if (!bankAccountOptional.isPresent()) {
            String json = "{\"message\":\"You don't have a bank account please contact admin\"}";
            return new ResponseEntity(json, HttpStatus.BAD_REQUEST);
        }
        BankAccount bankAccount = bankAccountOptional.get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("AccessKey", String.valueOf(bankAccount.getAccessKey()));
        headers.add("CountNumber", String.valueOf(bankAccount.getCountNumber()));
        //map.add("email", "first.last@example.com");
        //String json=objectMapper.writeValueAsString(rangeDto);
        HttpEntity request = new HttpEntity<>(data, headers);
        ResponseEntity<NormalTransactionsDto> rs = restTemplate.postForEntity(bankApi + "statement/statement", request, NormalTransactionsDto.class);
        NormalTransactionsDto normalTransactionsDto=rs.getBody();
        normalTransactionsDto.getListOfTransactions().stream().forEach(t -> t.setDescription(parseMessageFromBank(t.getDescription(), t.getSum())));
        System.out.println(rs.getBody());
        System.out.println(rs);
        return new ResponseEntity(rs.getBody(), rs.getStatusCode());
    }

    public String parseMessageFromBank(String message, BigDecimal sum){
        String result = message;
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Credential currentUser = credentialService.getByUsername(username).get();
        if(message.toLowerCase().contains("invoice") && (currentUser.getRole() == ROLE_USER)){
            message = message.replaceAll("\\D+", "");
            long invoiceId = Integer.valueOf(message);
            Invoice invoice = invoiceDao.getOne(invoiceId);
            long contractId = invoice.getContract().getId();
            result = "You paid invoice from date " + invoice.getFromDate() + " to date " + invoice.getTillDate() + " according to " +
                    "<a href='/user/profile?id=" + contractId + "'>contract nr." + contractId + "</a>.";
        }
        if(message.toLowerCase().contains("invoice") && (currentUser.getRole() == ROLE_COMPANY)){
            message = message.replaceAll("\\D+", "");
            long invoiceId = new Integer(message);
            Invoice invoice = invoiceDao.getOne(invoiceId);
            long contractId = invoice.getContract().getId();
            result = invoice.getContract().getUser().getName() + " " + invoice.getContract().getUser().getSurname()
                    +" has paid invoice from date " + invoice.getFromDate() + " to date " + invoice.getTillDate() + " for " + invoice.getContract().getService().getCategory().getName()
                    + ", " + invoice.getContract().getService().getTitle() + " subscription plan, according to " +
                    contractId + " contract no." + contractId;
        }
        if(message.toLowerCase().contains("sum")){
            sum = sum.setScale(2);
            result = "You added " + sum.toString() + " USD to your count.";
        }
        return result;
    }

    ;


    @PostMapping("/bank/balance")
    public ResponseEntity<BalanceDto> getBalance() {
        String username = authUtils.getAuthenticatedUsername();
        LOGGER.debug("user : " + username + " tries to add money to his account");
        Optional<BankAccount> bankAccountOptional = bankService.getBankAccountByUsername(username);
        if (!bankAccountOptional.isPresent()) {
            String json = "{\"message\":\"You don't have a bank account please contact admin\"}";
            return new ResponseEntity(json, HttpStatus.BAD_REQUEST);
        }
        BankAccount bankAccount = bankAccountOptional.get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("AccessKey", String.valueOf(bankAccount.getAccessKey()));
        headers.add("CountNumber", String.valueOf(bankAccount.getCountNumber()));
        //map.add("email", "first.last@example.com");
        HttpEntity request = new HttpEntity<>(headers);
            ResponseEntity rs = restTemplate.postForEntity(bankApi + "bankaccount/balance", request, BalanceDto.class);
        return new ResponseEntity(rs.getBody(), rs.getStatusCode());
    }

    ;

    @PostMapping("/invoice/payInvoice")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity payIds(@RequestBody Map<String, Object> map) throws JsonProcessingException {
        LOGGER.debug("ids: " + map.get("ids"));
        LOGGER.debug("id: " + map.get("id"));
        ResponseEntity resultResponseEntity;
        List ids = (List) map.get("ids");
        Object id = map.get("id");
        if (id instanceof String) {
            resultResponseEntity = payInvoice(Long.valueOf((String) id));
        } else if (ids instanceof List) {
            ids = (List<Long>) ids.stream().map(s -> Long.valueOf((String) s)).collect(Collectors.toList());
            resultResponseEntity = payInvoices(ids);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if (resultResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(resultResponseEntity.getBody(), resultResponseEntity.getStatusCode());
        }
    }

    //TODO when we will have a method that will confirm if a
    // trasaction was made with 2 accounts and a message then i will
    // check in the bank first , if it will not exist then i will pay it
    //this is very important when invoice was payed but right after banks or our internet was shut down ,or other consequesce
    private ResponseEntity payInvoice(Long id) throws JsonProcessingException {
        LOGGER.debug("trying to pay invoice with id: " + id);
        Optional<BankAccount> bankAccountOptional = bankService.getBankAccountByUsername(authUtils.getAuthenticatedUsername());
        if (!bankAccountOptional.isPresent()) {
            String json = "{\"message\":\"You don't have a bank account please contact admin\"}";
            return new ResponseEntity(json, HttpStatus.BAD_REQUEST);
        }
        BankAccount bankAccount = bankAccountOptional.get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("AccessKey", String.valueOf(bankAccount.getAccessKey()));
        headers.add("CountNumber", String.valueOf(bankAccount.getCountNumber()));

        Optional<InvoiceForPaymentDto> invoiceOptional = invoiceService.getFullInvoiceById(id);
        if (!invoiceOptional.isPresent()) {
            String json = "{\"message\":\"This invoice doesn't exist , please report to admins\"}";
            return new ResponseEntity(json, HttpStatus.BAD_REQUEST);
        }
        InvoiceForPaymentDto invoice = invoiceOptional.get();
        LOGGER.debug(invoice);
        if (!invoice.getUserUsername().equals(authUtils.getAuthenticatedUsername())) {
            String json = "{\"message\":\"This isn't your invoice,if you want to pay someones invoice , please contact admins.\"}";
            return new ResponseEntity(json, HttpStatus.BAD_REQUEST);
        }
        if (invoice.getStatus() == InvoiceStatus.CREATED) {
            String json = "{\"message\":\"This invoice is not valid.\"}";
            return new ResponseEntity(json, HttpStatus.BAD_REQUEST);
        }
        if (invoice.getStatus() == PAID) {
            String json = "{\"message\":\"This invoice was already paid.\"}";
            return new ResponseEntity(json, HttpStatus.BAD_REQUEST);
        }
        if (invoice.getStatus() == InvoiceStatus.OVERDUE) {
            String json = "{\"message\":\"You already overdued your payment.\"}";
            return new ResponseEntity(json, HttpStatus.BAD_REQUEST);
        }

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setCorrespondentCount(invoice.getCompanyBankCount());
        paymentDto.setDescription("Invoice " + invoice.getInvoiceId());
        paymentDto.setSum(invoice.getPrice());
        //String json = objectMapper.writeValueAsString(paymentDto);
        LOGGER.debug(paymentDto);
        HttpEntity request = new HttpEntity<>(paymentDto, headers);
        ResponseEntity<BalanceDto> rs = restTemplate.postForEntity(bankApi + "sendmoney/sendMoney", request, BalanceDto.class);
        if (rs.getStatusCode() == HttpStatus.OK) {
            System.out.println("rs:"+rs);
            invoiceService.makeInvoicePayed(invoice.getInvoiceId());
            sendNotification(invoice);
            //TODO create notifications
        }
        System.out.println(rs.getBody());
        return new ResponseEntity(rs.getBody(),rs.getStatusCode());
    }

    //TODO create notification for user and companies;
    private void sendNotification(InvoiceForPaymentDto invoiceForPaymentDto) {
        sendNotifications(Arrays.asList(invoiceForPaymentDto));
    }

    private ResponseEntity payInvoices(List<Long> ids) {
        LOGGER.debug("trying to pay  ids : " + ids);
        Optional<BankAccount> bankAccountOptional = bankService.getBankAccountByUsername(authUtils.getAuthenticatedUsername());
        if (!bankAccountOptional.isPresent()) {
            String json = "{\"message\":\"You don't have a bank account please contact admin\"}";
            return new ResponseEntity(json, HttpStatus.BAD_REQUEST);
        }
        BankAccount bankAccount = bankAccountOptional.get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("AccessKey", String.valueOf(bankAccount.getAccessKey()));
        headers.add("CountNumber", String.valueOf(bankAccount.getCountNumber()));
        List<PaymentDto> listOfPayments = new ArrayList<>();
        List<InvoiceForPaymentDto> invoiceForPaymentDtos = new ArrayList<>();
        for (Long id : ids) {
            Optional<InvoiceForPaymentDto> invoiceOptional = invoiceService.getFullInvoiceById(id);
            if (!invoiceOptional.isPresent()) {
                String json = "{\"message\":\"This invoice doesn't exist , please report to admins\"}";
                return new ResponseEntity(json, HttpStatus.BAD_REQUEST);
            }
            InvoiceForPaymentDto invoice = invoiceOptional.get();
            LOGGER.debug(invoice);
            if (!invoice.getUserUsername().equals(authUtils.getAuthenticatedUsername())) {
                String json = "{\"message\":\"This isn't your invoice,if you want to pay someones invoice , please contact admins.\"}";
                return new ResponseEntity(json, HttpStatus.BAD_REQUEST);
            }
            if (invoice.getStatus() == InvoiceStatus.CREATED) {
                String json = "{\"message\":\"This invoice is not valid.\"}";
                return new ResponseEntity(json, HttpStatus.BAD_REQUEST);
            }
            if (invoice.getStatus() == PAID) {
                String json = "{\"message\":\"This invoice was already paid.\"}";
                return new ResponseEntity(json, HttpStatus.BAD_REQUEST);
            }
            if (invoice.getStatus() == InvoiceStatus.OVERDUE) {
                String json = "{\"message\":\"You already overdued your payment.\"}";
                return new ResponseEntity(json, HttpStatus.BAD_REQUEST);
            }

            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setCorrespondentCount(invoice.getCompanyBankCount());
            paymentDto.setDescription("Invoice " + invoice.getInvoiceId());
            paymentDto.setSum(invoice.getPrice());
            listOfPayments.add(paymentDto);
            invoiceForPaymentDtos.add(invoice);
        }

        HttpEntity request = new HttpEntity<>(listOfPayments, headers);
        ResponseEntity rs = restTemplate.postForEntity(bankApi + "sendmoney/BulkPayment", request, Object.class);
        if (rs.getStatusCode() == HttpStatus.OK) {
            invoiceService.makeInvoicesPayed(ids);
            sendNotifications(invoiceForPaymentDtos);
            //TODO create notifications
        }
        return rs;
        //TODO implement when rest will be ready;
    }

    private void sendNotifications(List<InvoiceForPaymentDto> invoiceForPaymentDtos) {
        Credential adminCredential = credentialService.getDefaultAdminCredential();
        List notifications = new ArrayList<>();
        for (InvoiceForPaymentDto invoiceForPaymentDto : invoiceForPaymentDtos) {
            Notification companyNotification = notificationService.createNotificationPayedForCompany(invoiceForPaymentDto.getFullName(),
                    invoiceForPaymentDto.getInvoiceId(),
                    invoiceForPaymentDto.getServiceTitle(),
                    adminCredential, invoiceForPaymentDto.getCompanyCredential(),
                    LocalDateTime.now());
            Notification userNotification = notificationService.createNotificationPayedForUser(
                    invoiceForPaymentDto.getInvoiceId(),
                    invoiceForPaymentDto.getServiceTitle(),
                    adminCredential, invoiceForPaymentDto.getUserCredential(),
                    LocalDateTime.now());
            notifications.add(companyNotification);
            notifications.add(userNotification);
        }
        notificationService.saveNotifications(notifications);
    }


}
