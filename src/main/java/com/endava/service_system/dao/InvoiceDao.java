package com.endava.service_system.dao;

import com.endava.service_system.dto.InvoiceForPaymentDto;
import com.endava.service_system.dto.PaymentDto;
import com.endava.service_system.enums.InvoiceStatus;
import com.endava.service_system.model.Company;
import com.endava.service_system.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface InvoiceDao extends JpaRepository<Invoice,Long> {

    @Modifying
    @Query("UPDATE Invoice SET invoiceStatus=:invoiceStatus WHERE id in(:ids)")
    void setStatus(@Param("invoiceStatus")InvoiceStatus status, @Param("ids")List<Long> invoceIds);

    @Query("SELECT new com.endava.service_system.dto.InvoiceForPaymentDto(inv.id,inv.price,companyBankAccount.countNumber,userCredentials.username,inv.invoiceStatus) FROM Invoice inv INNER JOIN inv.contract contr " +
            "INNER JOIN contr.user user INNER JOIN contr.company company " +
            "INNER JOIN user.credential userCredentials INNER JOIN company.credential companyCredentials " +
            "INNER JOIN userCredentials.bankAccount userBankAccount INNER JOIN companyCredentials.bankAccount companyBankAccount WHERE inv.id=:id")
    Optional<InvoiceForPaymentDto> getFullInvoiceById(@Param("id") long id);
}
