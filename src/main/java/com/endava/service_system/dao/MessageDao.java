package com.endava.service_system.dao;

import com.endava.service_system.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MessageDao extends JpaRepository<Message,Long> {
    public Page<Message> getAllByOrderByDateDesc(Pageable pageable);

    @Modifying
    @Query("UPDATE Message SET read=:value WHERE id=:id")
    int markAsRead(@Param("id") long id,@Param("value") boolean value);
}
