package com.emse.spring.automacorp.model.dao;

import com.emse.spring.automacorp.model.SensorType;
import com.emse.spring.automacorp.model.entities.SensorEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class SensorDaoTest {
    @Autowired
    private SensorDao1 sensorDao;

    @Test
    public void shouldFindASensorById() {
        SensorEntity sensor = sensorDao.getReferenceById(-10L);
        Assertions.assertThat(sensor.getName()).isEqualTo("Temperature Room 1 Mines");
        Assertions.assertThat(sensor.getSensorType()).isEqualTo(SensorType.TEMPERATURE);
        Assertions.assertThat(sensor.getValue()).isEqualTo(21.3);
    }
}