package com.endava.service_system.dao;

import com.endava.service_system.model.entities.User;
import com.endava.service_system.model.enums.UserStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u INNER JOIN FETCH u.credential c WHERE c.username=:username")
    Optional<User> getByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u INNER JOIN FETCH u.credential c WHERE c.email=:email")
    Optional<User> getByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u INNER JOIN FETCH u.credential c")
    List<User> getAllWithCredentials();

    @Query("SELECT u FROM User u INNER JOIN FETCH u.credential c WHERE c.status=:status")
    List<User> getAllWithCredentialsAndStatus(@Param("status")UserStatus status);

    List<User> getByCredentialStatus(Pageable page, UserStatus status);

    List<User> getAllBy(Pageable page);

    long countByCredentialStatus(UserStatus userStatus);
}
