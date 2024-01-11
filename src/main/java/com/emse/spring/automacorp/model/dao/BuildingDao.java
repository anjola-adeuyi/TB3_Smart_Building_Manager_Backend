package com.emse.spring.automacorp.model.dao;

import com.emse.spring.automacorp.model.entities.BuildingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingDao extends JpaRepository<BuildingEntity, Long> {
}
