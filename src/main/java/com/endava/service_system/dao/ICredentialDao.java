package com.endava.service_system.dao;

import com.endava.service_system.model.Admin;
import com.endava.service_system.model.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICredentialDao extends JpaRepository<Credential,Long>{
	Optional<Credential> getByUsername(String username);
}
