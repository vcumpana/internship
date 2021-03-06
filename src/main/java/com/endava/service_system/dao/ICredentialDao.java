package com.endava.service_system.dao;

import com.endava.service_system.model.entities.Credential;
import com.endava.service_system.model.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

	Optional<Credential> getByEmail(String email);


	@Query("SELECT cr FROM Credential as cr JOIN Company as co ON cr.id = co.credential WHERE co.name = :companyName")
    Optional<Credential> getByCompanyName(@Param("companyName") String companyName);

	@Query("SELECT cr FROM Credential as cr JOIN User as u ON cr.id = u.credential WHERE u.id = :userId")
    Optional<Credential> getByUserId(@Param("userId") long userId);
}
