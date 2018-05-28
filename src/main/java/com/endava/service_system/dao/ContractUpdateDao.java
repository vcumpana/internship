package com.endava.service_system.dao;

import com.endava.service_system.model.entities.Contract;
import com.endava.service_system.model.entities.Invoice;
import com.endava.service_system.model.enums.ContractStatus;
import com.endava.service_system.model.enums.InvoiceStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

import static com.endava.service_system.model.enums.ContractStatus.ACTIVE;

@Repository
public class ContractUpdateDao {
    private static final int CONTRACT_DEFAULT_LIMIT=20;
    private static final String HQL_FOR_EXPIRED_CONTRACTS = "SELECT c FROM Contract c " +
            "WHERE c.status=:contractStatus " +
            "and c.endDate<:today";

    @PersistenceContext
    private EntityManager manager;

    public List<Contract> getActiveContractsThatHaveEndDateBefore(LocalDate currentDate,Integer limit){
        if(limit == null || limit < 1){
            limit = CONTRACT_DEFAULT_LIMIT;
        }
        Query query=manager.createQuery(HQL_FOR_EXPIRED_CONTRACTS);
        query.setParameter("today",currentDate);
        query.setParameter("contractStatus", ACTIVE);
        query.setMaxResults(limit);
        return query.getResultList();
    }
}
