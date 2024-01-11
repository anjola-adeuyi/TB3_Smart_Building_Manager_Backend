package com.emse.spring.automacorp.model.mappers;

import com.emse.spring.automacorp.model.entities.SensorEntity;
import com.emse.spring.automacorp.model.SensorType;
import com.emse.spring.automacorp.model.records.dao.Sensor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class SensorMapperTest {
    @Test
    void shouldMapSensor() {
        //Arrange
        SensorEntity currentTemperature = new SensorEntity(SensorType.TEMPERATURE, "Room temperature");
        currentTemperature.setId(1L);
        currentTemperature.setValue(24.2);

        //Act
        Sensor sensor = SensorMapper.of(currentTemperature);

        //Assert
        Sensor expectedSensor = new Sensor (
                1L,
                "Room temperature",
                24.2,
                SensorType.TEMPERATURE
        );
        Assertions.assertThat(sensor).usingRecursiveAssertion().isEqualTo(expectedSensor);
    }
}