package com.endava.service_system.dao;

import com.endava.service_system.model.dto.InvoiceForPaymentDto;
import com.endava.service_system.model.enums.InvoiceStatus;
import com.endava.service_system.model.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface InvoiceDao extends JpaRepository<Invoice,Long> {
    @Modifying
    @Query("UPDATE Invoice SET invoiceStatus=:invoiceStatus WHERE id in(:ids)")
    void setStatus(@Param("invoiceStatus")InvoiceStatus status, @Param("ids")List<Long> invoceIds);

    @Query("SELECT new com.endava.service_system.model.dto.InvoiceForPaymentDto(inv.id,inv.price,companyBankAccount.countNumber," +
            "inv.invoiceStatus,concat(user.name,' ',user.surname),userCredentials,companyCredentials,serv.title) " +
            "FROM Invoice inv JOIN inv.contract contr " +
            " JOIN contr.user user JOIN contr.company company " +
            " JOIN contr.service serv " +
            " JOIN user.credential userCredentials  JOIN company.credential companyCredentials " +
            " JOIN userCredentials.bankAccount userBankAccount  JOIN companyCredentials.bankAccount companyBankAccount WHERE inv.id=:id")
    Optional<InvoiceForPaymentDto> getFullInvoiceById(@Param("id") long id);
}
