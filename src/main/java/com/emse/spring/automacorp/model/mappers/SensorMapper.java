package com.emse.spring.automacorp.model.mappers;

import com.emse.spring.automacorp.model.entities.SensorEntity;
import com.emse.spring.automacorp.model.records.dao.Sensor;

public class SensorMapper {
    public static Sensor of(SensorEntity sensor) {
        return new Sensor(
                sensor.getId(),
                sensor.getName(),
                sensor.getValue(),
                sensor.getSensorType()
        );
    }
}
