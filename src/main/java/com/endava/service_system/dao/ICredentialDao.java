package com.endava.service_system.dao;

import com.endava.service_system.dto.CredentialDTO;
import com.endava.service_system.enums.UserStatus;
import com.endava.service_system.model.Admin;
import com.endava.service_system.model.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.server.PathParam;
import java.util.Optional;

@Repository
public interface ICredentialDao extends JpaRepository<Credential,Long>{
	Optional<Credential> getByUsername(String username);
	Credential save(Credential credential);
	@Transactional
	@Modifying
	@Query("UPDATE Credential SET password=:password,status=:status WHERE username=:username")
    int updateStatusAndPassword(@Param("username") String username,@Param("password") String password,@Param("status") UserStatus status);
	@Transactional
	@Modifying
	@Query("UPDATE Credential SET status=:status WHERE username=:username")
	int updateStatus(@Param("username") String username,@Param("status") UserStatus status);
	@Transactional
	@Modifying
	@Query("UPDATE Credential SET password=:password WHERE username=:username")
	int updatePassword(@Param("username") String username,@Param("password") String password);
}
