package com.endava.service_system.dao;

import com.endava.service_system.model.entities.Company;
import com.endava.service_system.model.entities.Contract;
import com.endava.service_system.model.entities.Service;
import com.endava.service_system.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface ContractDao extends JpaRepository<Contract,Long> {

    @Query("select c from Contract c join fetch c.company join fetch c.user where c.id=:id")
    Contract getContractWithDetails(@Param("id") long id);

    @Query("select c from Contract c join fetch c.company co join fetch co.credential cr where cr.username=:username")
    List<Contract> getAllContractsByCompanyUsername(@Param("username")String companyUsername);

    @Query("SELECT COUNT(c) FROM Contract AS c WHERE c.company = :company AND c.user = :user AND c.service = :service AND c.endDate > :startDate AND (c.status = 'ACTIVE' OR c.status = 'SIGNEDBYCLIENT')")
    int checkIfSuchContractExists(@Param("company") Company company, @Param("user") User user, @Param("service") Service service, @Param("startDate")LocalDate startDate);
}
