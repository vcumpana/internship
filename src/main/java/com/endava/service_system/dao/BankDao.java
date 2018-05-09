package com.endava.service_system.dao;

import com.endava.service_system.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankDao extends JpaRepository<BankAccount,Long> {

    @Query(" SELECT b FROM Credential c INNER JOIN c.bankAccount b WHERE c.username=:username")
    Optional<BankAccount> getBankAccountByUsername(@Param("username") String username);
}
