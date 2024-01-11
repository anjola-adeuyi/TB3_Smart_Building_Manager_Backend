package com.emse.spring.automacorp.model.dao;

import com.emse.spring.automacorp.model.entities.HeaterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeaterDao extends JpaRepository<HeaterEntity, Long>, HeaterDaoCustom {
}
