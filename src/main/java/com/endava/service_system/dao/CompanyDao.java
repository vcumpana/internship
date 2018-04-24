package com.endava.service_system.dao;

import com.endava.service_system.model.Company;
import com.endava.service_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.websocket.server.PathParam;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface CompanyDao extends JpaRepository<Company,Long> {

    Optional<Company> getById(Long aLong);

    @Query("SELECT c FROM Company c INNER JOIN FETCH c.credential cr WHERE cr.username=:username")
    Optional<Company> getByUsername(@Param("username") String username);

//    @Query("SELECT c FROM Company c INNER JOIN FETCH c.credential cr WHERE cr.email=:email")
//    Optional<Company> getByEmail(@PathParam("email") String email);

    Optional<Company> getByEmail(String email);
}
