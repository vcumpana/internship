package com.endava.service_system.dao;

import com.endava.service_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.websocket.server.PathParam;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u INNER JOIN FETCH u.credential c WHERE c.username=:username")
    Optional<User> getByUsername(@Param("username") String username);
}