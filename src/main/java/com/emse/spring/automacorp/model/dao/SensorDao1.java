package com.emse.spring.automacorp.model.dao;

import com.emse.spring.automacorp.model.entities.SensorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorDao1 extends JpaRepository<SensorEntity, Long> {
    //The name is different since there was an error in the SensorDaoTest that didn't find this interface if SensorDao was the name...
}
