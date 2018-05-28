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

    List<Service> getByTitleAndIdIsNot(String title, long id);

    @Query(value = "select s.id as id, s.description as description , s.price as price, s.title as title, c.name as companyName" +
            ", cat.name as category, c.company_url, c.image_name from service s " +
            "join company_services cs " +
            "on cs.services_id = s.id " +
            "join company c " +
            "on c.id = cs.company_id " +
            "join categories cat " +
            "on cat.id = s.category_id", nativeQuery = true)
    List<Map> getAll();

    @Query(value = "select s.id as id, s.description as description , s.price as price, s.title as title, c.name as companyName, cat.name as category from service s " +
            "join company_services cs " +
            "on cs.services_id = s.id " +
            "join company c " +
            "on c.id = cs.company_id " +
            "join categories cat " +
            "on cat.id = s.category_id WHERE cat.name=:categoryName", nativeQuery = true)
    List<Map> getByCategoryName(@Param("categoryName") String categoryName);

    @Transactional
    @Modifying
    @Query(value = "delete from Service s where s.id=:id")
    int deleteServicesById(@Param("id") long id);

    @Query(value = "select s.id as id, s.description as description , s.price as price, s.title as title, c.name as companyName, cat.name as category, c.company_url as company_url,c.image_name as image_name from service s " +
            "join company_services cs " +
            "on cs.services_id = s.id " +
            "join company c " +
            "on c.id = cs.company_id " +
            "join categories cat " +
            "on cat.id = s.category_id WHERE s.id=:id", nativeQuery = true)
    List<Map> getServiceDtoById(@Param("id") long id);

    @Query(value = "select count (c) as count from Contract c " +
            "join c.service s " +
            "join c.company co " +
            "where s.title=:title and co.name =:companyName and c.status='ACTIVE'")
    int countContracts(@Param("title") String title, @Param("companyName") String companyName);
}
