package com.endava.service_system.dao;

import com.endava.service_system.model.entities.BankKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankKeyDao extends JpaRepository<BankKey,Long> {

}
