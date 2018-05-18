package com.endava.service_system.dao;

import com.endava.service_system.model.entities.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageDao extends JpaRepository<ImageEntity,Long> {

    Optional<ImageEntity> findByNameEquals(String name);
}
