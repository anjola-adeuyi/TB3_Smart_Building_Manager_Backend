package com.emse.spring.automacorp.model.dao;

import com.emse.spring.automacorp.model.entities.WindowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WindowDao extends JpaRepository<WindowEntity, Long>, WindowDaoCustom {
}
