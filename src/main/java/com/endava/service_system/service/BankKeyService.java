package com.endava.service_system.service;

import com.endava.service_system.dao.BankKeyDao;
import com.endava.service_system.model.entities.BankKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankKeyService {

    private final BankKeyDao bankKeyDao;

    public BankKey save(BankKey bankKey){
        return bankKeyDao.save(bankKey);
    }

}
