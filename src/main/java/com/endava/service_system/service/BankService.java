package com.endava.service_system.service;

import com.endava.service_system.controller.rest.BankRestController;
import com.endava.service_system.dao.BankDao;
import com.endava.service_system.dao.InvoiceDao;
import com.endava.service_system.exception.BankProblemException;
import com.endava.service_system.model.dto.BankAccountDto;
import com.endava.service_system.model.entities.BankAccount;
import com.endava.service_system.model.entities.BankKey;
import com.endava.service_system.model.entities.Credential;
import com.endava.service_system.model.entities.Invoice;
import com.endava.service_system.utils.EncryptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Optional;

import static com.endava.service_system.model.enums.Role.ROLE_USER;

@Service
public class BankService {
    private static final Logger LOGGER=LogManager.getLogger(BankRestController.class);
    private RestTemplate restTemplate;
    private BankDao bankDao;
    private CredentialService credentialService;
    private @Qualifier("bankApi") String bankApi;
    private EncryptionUtils encryptionUtils;
    private BankKeyService bankKeyService;
    private InvoiceDao invoiceDao;

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
    public void setEncryptionUtils(EncryptionUtils encryptionUtils) {
        this.encryptionUtils = encryptionUtils;
    }

    @Autowired
    public void setBankApi(@Qualifier("bankApi") String bankApi) {
        this.bankApi = bankApi;
    }

    @Autowired
    public void setBankKeyService(BankKeyService bankKeyService) {
        this.bankKeyService = bankKeyService;
    }

    @Autowired
    public void setInvoiceDao(InvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
    }

    @Transactional
    public void addBankAccount(Credential credential) throws InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        if(credential.getBankAccount()!=null){
            return;
        }
        Optional<BankAccount> bankAccountOptional=createBankAccountWithRest();
        bankAccountOptional.orElseThrow(()->new BankProblemException());
        save(bankAccountOptional.get(),credential);
    };

    public Optional<BankAccount> getBankAccountByUsername(String username){
        return bankDao.getBankAccountByUsername(username);
    }

    private void save(BankAccount bankAccount, Credential credential){
        BankKey keys=bankKeyService.save(bankAccount.getBankKeys());
        bankAccount.setBankKeys(keys);
        BankAccount saveBankAccount=bankDao.save(bankAccount);
        credential.setBankAccount(saveBankAccount);
        credentialService.save(credential);
    }

    public Optional<BankAccount> createBankAccountWithRest() throws InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        LOGGER.debug("trying to createBankAccount : ");
        //ResponseEntity rs=restTemplate.postForEntity(bankApi+"bankaccount/create",null,ResponseEntity.class,new HashMap<>());
        KeyPair keyPair=encryptionUtils.generateKeyPair();
        RSAPublicKey publicKey= (RSAPublicKey) keyPair.getPublic();
        String modulus=encryptionUtils.getModulus(publicKey);
        HttpHeaders headers = new HttpHeaders();
        encryptionUtils.init(null,keyPair.getPrivate().getEncoded());
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity request = new HttpEntity<>(modulus,headers);
        //map.add("email", "first.last@example.com");
        ResponseEntity<Object> response = restTemplate.postForEntity( bankApi+"bankaccount/create", request , Object.class );
        LOGGER.debug(request);
        LOGGER.debug(response);
        if(response.getStatusCode()!=HttpStatus.OK){
            return Optional.empty();
        }
        BankAccountDto bankAccountDto=(BankAccountDto)encryptionUtils.decryptData(response.getBody().toString(),BankAccountDto.class);
        BankAccount bankAccount=new BankAccount();
        bankAccount.setCountNumber(bankAccountDto.getCountNumber());
        RSAPrivateKey privateKey= (RSAPrivateKey) keyPair.getPrivate();
        BankKey keys=new BankKey();
        //encryptionUtils.init(Base64.getDecoder().decode(bankAccountDto.getBankPublicKeyModulus()),privateKey.getEncoded());
        byte[] bankModulus=Base64.getDecoder().decode(bankAccountDto.getBankPublicKeyModulus());
        keys.setBankModulus(bankModulus);
        keys.setJavaPrivateKey(privateKey.getEncoded());
        bankAccount.setBankKeys(keys);
        //TODO save public key , private key, account NR in db
        return Optional.of(bankAccount);
    }

    public String parseMessageFromBank(String message, BigDecimal sum){
        String result = message;
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Credential currentUser = credentialService.getByUsername(username).get();
        if(message.toLowerCase().contains("invoice") && (currentUser.getRole() == ROLE_USER)){
            message = message.replaceAll("\\D+", "");
            long invoiceId = new Integer(message);
            Invoice invoice = invoiceDao.getOne(invoiceId);
            long contractId = invoice.getContract().getId();
            result = "You paid invoice from date " + invoice.getFromDate() + " to date " + invoice.getTillDate() + " according to " +
                    "<a href='/user/profile?id=" + contractId + "'>contract nr." + contractId + "</a>.";
        }
        if(message.toLowerCase().contains("sum") && (currentUser.getRole() == ROLE_USER)){
            sum = sum.setScale(2);
            result = "You added " + sum.toString() + " USD to your count.";
        }
        return result;
    }
}
