package com.endava.service_system.service;

import com.endava.service_system.dao.CurrentDateDao;
import com.endava.service_system.model.CurrentDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CurrentDateService {

    private final CurrentDateDao currentDateDao;
    public CurrentDate getCurrentDate(){
        return currentDateDao.getTopByIdOrderByIdAsc(1L);
    }
}
