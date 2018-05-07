package com.endava.service_system.dao;

import com.endava.service_system.model.CurrentDate;
import com.endava.service_system.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface CurrentDateDao extends JpaRepository<CurrentDate,Long> {
}
