package com.emse.spring.automacorp.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {
    @Query("SELECT MIN(m.time) FROM Measurement m")
    String findOldestTime();

    @Query("SELECT MAX(m.time) FROM Measurement m")
    String findEarliestTime();
}
