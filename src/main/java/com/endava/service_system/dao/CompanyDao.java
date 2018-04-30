package com.endava.service_system.dao;

import com.endava.service_system.enums.UserStatus;
import com.endava.service_system.model.Company;
import com.endava.service_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface CompanyDao extends JpaRepository<Company,Long> {

    Optional<Company> getById(int aLong);

    @Query("SELECT c FROM Company c INNER JOIN FETCH c.credential cr WHERE cr.username=:username")
    Optional<Company> getByUsername(@Param("username") String username);

    @Query("SELECT c FROM Company c INNER JOIN FETCH c.credential cr WHERE cr.status=:status")
    List<Company> getAllWithStatus(@Param("status")UserStatus userStatus);

    @Query("SELECT c FROM Company c join fetch c.services s")
    List<Company> getAll();

    @Query("SELECT c FROM Company c INNER JOIN FETCH c.credential cr JOIN FETCH c.services s WHERE cr.username=:username")
    Optional<Company> getCompanyByNameWithServices(@Param("username")String name);

    Optional<Company> getByName(String name);

    Optional<Company> getByEmail(String email);

    @Query("SELECT cr.username FROM Company c INNER JOIN c.credential cr WHERE c.name=:name")
    Optional<String> getCredentialUsernameByName(@Param("name") String name);

}
