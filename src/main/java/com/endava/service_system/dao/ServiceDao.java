package com.endava.service_system.dao;

import com.endava.service_system.model.entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ServiceDao extends JpaRepository<Service,Long> {

    Optional<Service> getById(long id);

    Optional<Service> getByTitle(String title);

    Optional<Service> getByTitleAndId(String title, long id);

    List<Service> getByTitleAndIdIsNot(String title, long id);

    @Query(value = "SELECT s.id AS id, s.description AS description , s.price AS price, s.title AS title, c.name AS companyName" +
            ", cat.name AS category, c.url FROM service s " +
            "JOIN company_services cs " +
            "ON cs.services_id = s.id " +
            "JOIN company c " +
            "ON c.id = cs.company_id " +
            "JOIN categories cat " +
            "ON cat.id = s.category_id", nativeQuery = true)
    List<Map> getAll();

    @Query(value = "SELECT s.id AS id, s.description AS description , s.price AS price, s.title AS title, c.name AS companyName, cat.name AS category from service s " +
            "JOIN company_services cs " +
            "ON cs.services_id = s.id " +
            "JOIN company c " +
            "ON c.id = cs.company_id " +
            "JOIN categories cat " +
            "ON cat.id = s.category_id WHERE cat.name=:categoryName", nativeQuery = true)
    List<Map> getByCategoryName(@Param("categoryName") String categoryName);

    @Transactional
    @Modifying
    @Query(value = "delete from Service s WHERE s.id=:id")
    int deleteServicesById(@Param("id") long id);

    @Query(value = "SELECT s.id AS id, s.description AS description , s.price AS price, s.title AS title, c.name AS companyName, cat.name AS category, c.company_url AS company_url,c.image_name AS image_name from service s " +
            "JOIN company_services cs " +
            "ON cs.services_id = s.id " +
            "JOIN company c " +
            "ON c.id = cs.company_id " +
            "JOIN categories cat " +
            "ON cat.id = s.category_id WHERE s.id=:id", nativeQuery = true)
    List<Map> getServiceDtoById(@Param("id") long id);

    @Query(value = "SELECT COUNT (c) AS count FROM Contract c " +
            "JOIN c.service s " +
            "JOIN c.company co " +
            "WHERE s.title=:title AND co.name =:companyName")
    int countContracts(@Param("title") String title, @Param("companyName") String companyName);
}
