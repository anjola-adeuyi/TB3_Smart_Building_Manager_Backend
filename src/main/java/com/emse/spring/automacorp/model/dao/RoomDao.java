package com.emse.spring.automacorp.model.dao;

import com.emse.spring.automacorp.model.entities.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomDao extends JpaRepository<RoomEntity, Long>, RoomDaoCustom {
}
