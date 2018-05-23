package com.endava.service_system.controller.rest;

import com.endava.service_system.model.dto.*;
import com.endava.service_system.model.entities.BankAccount;
import com.endava.service_system.model.entities.BankKey;
import com.endava.service_system.model.entities.Credential;
import com.endava.service_system.model.entities.Notification;
import com.endava.service_system.model.enums.InvoiceStatus;
import com.endava.service_system.service.BankService;
import com.endava.service_system.service.CredentialService;
import com.endava.service_system.service.InvoiceService;
import com.endava.service_system.service.NotificationService;
import com.endava.service_system.utils.AuthUtils;
import com.endava.service_system.utils.EncryptionUtils;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.endava.service_system.model.enums.InvoiceStatus.PAID;

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
    private EncryptionUtils encryptionUtils;

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
    public void setEncryptionUtils(EncryptionUtils encryptionUtils) {
        this.encryptionUtils = encryptionUtils;
    }

    //TODO add this paths to spring security;
    @PostMapping("/bank/addmoney")
    public ResponseEntity<Object> addMoneyToBankAccount(@RequestBody Map<String, Object> data) throws InvalidKeySpecException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        Number sum = (Number) data.get("sum");
        if (sum.doubleValue() < 0) {
            String json = "{\"message\":\"Sum should be bigger than 0 \"}";
            return new ResponseEntity<Object>(json, HttpStatus.BAD_REQUEST);
        }
        String username = authUtils.getAuthenticatedUsername();
        LOGGER.debug("user : " + username + " tries to add money to his account");
        Optional<BankAccount> bankAccountOptional = bankService.getBankAccountByUsername(username);
        if (!bankAccountOptional.isPresent()) {
            String json = "{\"message\":\"You don't have a bank account please contact admin\"}";
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        }
        BankAccount bankAccount = bankAccountOptional.get();
        BankKey bankKey=bankAccount.getBankKeys();
        encryptionUtils.init(bankKey);
        AddMoneyDto addMoneyDto = new AddMoneyDto();
        addMoneyDto.setSum(sum.doubleValue());
        String encryptedData=encryptionUtils.encryptData(addMoneyDto);
        HttpEntity<String> request = new HttpEntity<String>(encryptedData, createHeader(bankAccount.getCountNumber()));
        ResponseEntity rs = restTemplate.postForEntity(bankApi + "bankaccount/addmoney", request,Object.class);
        String encoded=rs.getBody().toString();
        BalanceDto balanceDto= (BalanceDto) encryptionUtils.decryptData(encoded,BalanceDto.class);
        return new ResponseEntity<Object>(balanceDto, rs.getStatusCode());
    }

    private HttpHeaders createHeader(long countNr){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("CountNumber", String.valueOf(countNr));
        return headers;
    }

    @PostMapping("/bank/addbankaccount")
    public ResponseEntity<String> addBankAccount(Authentication authentication) throws InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {

        Optional<Credential> credentialOptional = credentialService.getByUsername(authentication.getName());
        if (!credentialOptional.isPresent()) {
            String json = "{\"message\":\"Bank Problem\"}";
            return new ResponseEntity<>(json, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        bankService.addBankAccount(credentialOptional.get());
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @PostMapping("/bank/statements")
    public ResponseEntity<Object> getStatements(@RequestBody Map<String, Object> data) throws IOException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        String username = authUtils.getAuthenticatedUsername();
        LOGGER.debug("user : " + username + " tries to see his statements");
        LOGGER.debug("range : " + data);
        Optional<BankAccount> bankAccountOptional = bankService.getBankAccountByUsername(username);
        if (!bankAccountOptional.isPresent()) {
            String json = "{\"message\":\"You don't have a bank account please contact admin\"}";
            return new ResponseEntity(json, HttpStatus.BAD_REQUEST);
        }
        BankAccount bankAccount = bankAccountOptional.get();
        BankKey bankKey=bankAccount.getBankKeys();
        encryptionUtils.init(bankKey);
        String encryptedData=encryptionUtils.encryptData(data);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("CountNumber", String.valueOf(bankAccount.getCountNumber()));
        //map.add("email", "first.last@example.com");
        //String json=objectMapper.writeValueAsString(rangeDto);
        HttpEntity<Object> request = new HttpEntity<>(encryptedData, headers);
        ResponseEntity rs = restTemplate.postForEntity(bankApi + "statement/statement", request, Object.class);
        System.out.println(rs.getBody());
        System.out.println(rs);
        String encoded=rs.getBody().toString();
        ShortTransactionsDto shortTransactionsDto= (ShortTransactionsDto) encryptionUtils.decryptData(encoded,ShortTransactionsDto.class);
        NormalTransactionsDto normalTransactions=new NormalTransactionsDto();
        normalTransactions.setPages(shortTransactionsDto.getPages());
        normalTransactions.setBalanceBefore(shortTransactionsDto.getBalanceBefore());
        normalTransactions.setListOfTransactions(
            shortTransactionsDto.getListOfTransactions()
                    .stream().map(tr-> {
                NormalTransaction nr=new NormalTransaction();
                nr.setCorrespondentCount(tr.getC());
                nr.setDate(tr.getD());
                nr.setDescription(tr.getDr());
                nr.setMainCount(tr.getM());
                nr.setSum(tr.getS());
                return nr;
            }).collect(Collectors.toList())


        );
        return new ResponseEntity<Object>(normalTransactions, rs.getStatusCode());
    }

    @PostMapping("/bank/balance")
    public ResponseEntity<BalanceDto> getBalance() throws InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        String username = authUtils.getAuthenticatedUsername();
        LOGGER.debug("user : " + username + " tries to add money to his account");
        Optional<BankAccount> bankAccountOptional = bankService.getBankAccountByUsername(username);
        if (!bankAccountOptional.isPresent()) {
            String json = "{\"message\":\"You don't have a bank account please contact admin\"}";
            return new ResponseEntity(json, HttpStatus.BAD_REQUEST);
        }
        BankAccount bankAccount = bankAccountOptional.get();
        BankKey key=bankAccount.getBankKeys();
        encryptionUtils.init(key);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        headers.add("CountNumber", String.valueOf(bankAccount.getCountNumber()));
        //map.add("email", "first.last@example.com");
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity rs = restTemplate.postForEntity(bankApi + "bankaccount/balance", request, Object.class);
        BalanceDto balanceDto=(BalanceDto)encryptionUtils.decryptData(rs.getBody().toString(), BalanceDto.class);
        return new ResponseEntity<>(balanceDto, rs.getStatusCode());
    }

    ;

    @PostMapping("/invoice/payInvoice")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity payIds(@RequestBody Map<String, Object> map) throws IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        LOGGER.debug("ids: " + map.get("ids"));
        LOGGER.debug("id: " + map.get("id"));
        ResponseEntity resultResponseEntity;
        List ids = (List) map.get("ids");
        Object id = map.get("id");
        if (id instanceof String) {
            resultResponseEntity = payInvoices(Arrays.asList(Long.valueOf((String) id)));
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

    private ResponseEntity payInvoices(List<Long> ids) throws InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        LOGGER.debug("trying to pay  ids : " + ids);
        Optional<BankAccount> bankAccountOptional = bankService.getBankAccountByUsername(authUtils.getAuthenticatedUsername());
        if (!bankAccountOptional.isPresent()) {
            String json = "{\"message\":\"You don't have a bank account please contact admin\"}";
            return new ResponseEntity(json, HttpStatus.BAD_REQUEST);
        }
        BankAccount bankAccount = bankAccountOptional.get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
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
        BankKey bankKey=bankAccount.getBankKeys();
        encryptionUtils.init(bankKey);
        String encrypted= encryptionUtils.encryptData(listOfPayments);
        System.out.println(encrypted);
        HttpEntity<String> request = new HttpEntity<>(encrypted, headers);
        ResponseEntity rs = restTemplate.postForEntity(bankApi + "sendmoney/BulkPayment", request, Object.class);
        BalanceDto balanceDto=(BalanceDto)encryptionUtils.decryptData(rs.getBody().toString(),BalanceDto.class);
        if (rs.getStatusCode() == HttpStatus.OK) {
            invoiceService.makeInvoicesPayed(ids);
            sendNotifications(invoiceForPaymentDtos);
            //TODO create notifications
        }
        return new ResponseEntity(balanceDto,rs.getStatusCode());
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
