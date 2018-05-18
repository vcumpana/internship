package com.endava.service_system.dao;

import com.endava.service_system.model.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Repository
public interface ICategoryDao extends JpaRepository<Category,Integer> {
    Optional<Category> getByName(String name);
    @Transactional
    List<Category> deleteByName(String name);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Category SET name=:newName WHERE name=:oldName")
    int updateCategoryByName(@Param("oldName")String oldName, @Param("newName")String newName);
}
