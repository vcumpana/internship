package com.endava.service_system.dao;

import com.endava.service_system.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface CompanyDao extends JpaRepository<Company,Long> {

}
