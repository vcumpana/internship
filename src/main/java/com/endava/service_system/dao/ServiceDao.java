package com.endava.service_system.dao;

import com.endava.service_system.dto.ServiceToUserDto;
import com.endava.service_system.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ServiceDao extends JpaRepository<Service,Long> {

    Optional<Service> getById(int id);

//    @Query("SELECT c FROM Company c INNER JOIN FETCH c.credential cr WHERE cr.email=:email")
//    Optional<Company> getByEmail(@PathParam("email") String email);

    //@Query("SELECT c.se FROM Com s join fetch s.company c WHERE c.name=:companyName")
   // List<Service> getByCompanyName(@PathParam("companyName") String companyName);

    Optional<Service> getByTitle(String title);

    @Query(value = "select s.id as id, s.description as description , s.price as price, s.title as title, c.name as companyName, cat.name as category from service s " +
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

    Optional<Service> deleteServicesById(int id);
}
