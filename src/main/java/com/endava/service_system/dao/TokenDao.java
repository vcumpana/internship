package com.endava.service_system.dao;

import com.endava.service_system.model.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenDao extends JpaRepository<Token,Long>{

    Optional<Token> getTokenByTokenEquals(String token);

}
