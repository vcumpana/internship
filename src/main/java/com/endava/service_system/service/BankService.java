package com.endava.service_system.service;

import com.endava.service_system.controller.rest.BankRestController;
import com.endava.service_system.dao.BankDao;
import com.endava.service_system.exception.BankProblemException;
import com.endava.service_system.model.BankAccount;
import com.endava.service_system.model.Credential;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class BankService {
    private static final Logger LOGGER=LogManager.getLogger(BankRestController.class);
    private RestTemplate restTemplate;
    private BankDao bankDao;
    private CredentialService credentialService;
    private @Qualifier("bankApi") String bankApi;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public void setBankDao(BankDao bankDao) {
        this.bankDao = bankDao;
    }

    @Autowired
    public void setCredentialService(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @Autowired
    public void setBankApi(@Qualifier("bankApi") String bankApi) {
        this.bankApi = bankApi;
    }

    public void addBankAccount(Credential credential){
        if(credential.getBankAccount()!=null){
            return;
        }
        Optional<BankAccount> bankAccountOptional=createBankAccount();
        bankAccountOptional.orElseThrow(()->new BankProblemException());
        save(bankAccountOptional.get(),credential);
    };

    public Optional<BankAccount> getBankAccountByUsername(String username){
        return bankDao.getBankAccountByUsername(username);
    }

    private Optional<BankAccount> createBankAccount(){
        ResponseEntity<BankAccount> rs= createBankAccountWithRest();
        LOGGER.debug(rs);
        if(rs.getStatusCode()==HttpStatus.OK){
            return Optional.ofNullable(rs.getBody());
        }
        return Optional.empty();
    };

    private void save(BankAccount bankAccount,Credential credential){
        BankAccount saveBankAccount=bankDao.save(bankAccount);
        credential.setBankAccount(saveBankAccount);
        credentialService.save(credential);
    }

    private ResponseEntity<BankAccount> createBankAccountWithRest(){
        LOGGER.debug("trying to createBankAccount : ");
        //ResponseEntity rs=restTemplate.postForEntity(bankApi+"bankaccount/create",null,ResponseEntity.class,new HashMap<>());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        //map.add("email", "first.last@example.com");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity response = restTemplate.postForEntity( bankApi+"bankaccount/create", request , BankAccount.class );
        LOGGER.debug(request);
        return response;
    }
}
