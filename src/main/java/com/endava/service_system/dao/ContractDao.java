package com.endava.service_system.dao;

import com.endava.service_system.model.entities.Company;
import com.endava.service_system.model.entities.Contract;
import com.endava.service_system.model.entities.Service;
import com.endava.service_system.model.entities.User;
import com.endava.service_system.model.enums.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface ContractDao extends JpaRepository<Contract, Long> {

    @Query("SELECT c FROM Contract c JOIN fetch c.company JOIN fetch c.user WHERE c.id=:id")
    Contract getContractWithDetails(@Param("id") long id);

    @Query("SELECT c FROM Contract c JOIN fetch c.company co JOIN fetch co.credential cr WHERE cr.username=:username")
    List<Contract> getAllContractsByCompanyUsername(@Param("username") String companyUsername);

    @Query("SELECT COUNT(c) FROM Contract AS c WHERE c.company = :company AND c.user = :user AND c.service = :service AND c.endDate > :startDate AND (c.status = 'ACTIVE' OR c.status = 'SIGNEDBYCLIENT')")
    int checkIfSuchContractExists(@Param("company") Company company, @Param("user") User user, @Param("service") Service service, @Param("startDate") LocalDate startDate);

    @Query("SELECT c.id FROM Contract c " +
            "JOIN c.company co " +
            "JOIN co.credential cr WHERE cr.username=:username and c.status='ACTIVE'")
    long[] getAllIds(@Param("username") String username);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Contract SET status=:status WHERE id IN (:ids)")
    void setStatus(@Param("status") ContractStatus expired, @Param("ids") List<Long> contractIds);
}
