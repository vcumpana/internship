package com.endava.service_system.controller.rest;

import com.endava.service_system.dto.*;
import com.endava.service_system.enums.InvoiceStatus;
import com.endava.service_system.exception.BankProblemException;
import com.endava.service_system.model.BankAccount;
import com.endava.service_system.model.Credential;
import com.endava.service_system.model.Invoice;
import com.endava.service_system.model.Statement;
import com.endava.service_system.service.BankService;
import com.endava.service_system.service.CredentialService;
import com.endava.service_system.service.InvoiceService;
import com.endava.service_system.utils.AuthUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.mapper.Mapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.endava.service_system.enums.InvoiceStatus.PAID;

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

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public void setBankApi(String bankApi) {
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
        return new ResponseEntity(rs.getBody(),rs.getStatusCode());
    }

    ;

    @PostMapping("/bank/statements")
    public ResponseEntity<List> getStatements(@RequestBody StatementRangeDto rangeDto) throws JsonProcessingException {

        String username = authUtils.getAuthenticatedUsername();
        LOGGER.debug("user : " + username + " tries to see his statements");
        LOGGER.debug("range : " + rangeDto);
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
        String json = objectMapper.writeValueAsString(rangeDto);
        HttpEntity request = new HttpEntity<>(json, headers);
        return restTemplate.postForEntity(bankApi + "statement/statement", request, List.class);
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
        ResponseEntity rs= restTemplate.postForEntity(bankApi + "bankaccount/balance", request, BalanceDto.class);
        return new ResponseEntity(rs.getBody(),rs.getStatusCode());
    }

    ;

    @PostMapping("/invoice/payInvoice")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity payIds(@RequestBody Map<String, Object> map) throws JsonProcessingException {
        LOGGER.debug("ids: " + map.get("ids"));
        LOGGER.debug("id: " + map.get("id"));
        ResponseEntity resultResponseEntity;
        Object ids = map.get("ids");
        Object id = map.get("id");
        if (id instanceof Integer) {
            resultResponseEntity = payInvoice((Integer) id);
        } else if (ids instanceof Integer[]) {
            resultResponseEntity = payInvoices((Integer[]) ids);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if (resultResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return resultResponseEntity;
        }
    }

    //TODO when we will have a method that will confirm if a
    // trasaction was made with 2 accounts and a message then i will
    // check in the bank first , if it will not exist then i will pay it
    //this is very important when invoice was payed but right after banks or our internet was shut down ,or other consequesce
    private ResponseEntity payInvoice(Integer integer) throws JsonProcessingException {
        LOGGER.debug("trying to pay invoice with id: " + integer);
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

        Optional<InvoiceForPaymentDto> invoiceOptional = invoiceService.getFullInvoiceById(integer);
        if (!invoiceOptional.isPresent()) {
            String json = "{\"message\":\"This invoice doesn't exist , please report to admins\"}";
            return new ResponseEntity(json, HttpStatus.BAD_REQUEST);
        }
        InvoiceForPaymentDto invoice = invoiceOptional.get();
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
        paymentDto.setDescription("Invoice " + invoice.getId());
        paymentDto.setSum(invoice.getPrice());
        String json = objectMapper.writeValueAsString(paymentDto);
        HttpEntity request = new HttpEntity<>(json, headers);
        ResponseEntity rs = restTemplate.postForEntity(bankApi + "/sendMoney", request, String.class);
        if (rs.getStatusCode() == HttpStatus.OK) {
            invoiceService.makeInvoicePayed(invoice.getId());
            //TODO create notifications
        }
        return rs;
    }

    private ResponseEntity payInvoices(Integer[] ids) {
        LOGGER.debug("trying to pay : " + ids);
        throw new RuntimeException("Not implemented");
        //TODO implement when rest will be ready;
    }


}
