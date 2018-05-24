package com.endava.service_system.dao;

import com.endava.service_system.model.enums.UserStatus;
import com.endava.service_system.model.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface CompanyDao extends JpaRepository<Company,Long> {

    Optional<Company> getById(long id);

    @Query("SELECT c FROM Company c INNER JOIN FETCH c.credential cr WHERE cr.username=:username")
    Optional<Company> getByUsername(@Param("username") String username);

    @Query("SELECT c FROM Company c INNER JOIN FETCH c.credential cr WHERE cr.status=:status")
    List<Company> getAllWithStatus(@Param("status")UserStatus userStatus);

    @Query("SELECT c FROM Company c LEFT join fetch c.services s")
    List<Company> getAll();

    @Query("SELECT c FROM Company c LEFT JOIN c.services s WHERE c.name=:name")
    Optional<Company> getCompanyByNameWithServices(@Param("name")String name);

    Optional<Company> getByName(String name);

    @Query("SELECT c FROM Company c INNER JOIN c.credential cred WHERE cred.email=:email")
    Optional<Company> getByEmail(@Param("email")String email);

    @Query("SELECT cr.username FROM Company c INNER JOIN c.credential cr WHERE c.name=:name")
    Optional<String> getCredentialUsernameByName(@Param("name") String name);
}
