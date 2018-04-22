package com.endava.service_system.dao;

import java.util.Optional;

import com.endava.service_system.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAdminDao extends JpaRepository<Admin,Long>{
	Optional<Admin> getByUsername(String username);
}
