package com.emse.spring.automacorp.model.mappers;

import com.emse.spring.automacorp.model.entities.HeaterEntity;
import com.emse.spring.automacorp.model.records.dao.Heater;
import com.emse.spring.automacorp.model.HeaterStatus;

public class HeaterMapper {
    public static Heater of(HeaterEntity heater) {
        return new Heater(
                heater.getId(),
                heater.getName(),
                heater.getRoom().getId(),
                SensorMapper.of(heater.getStatus()).value() == 1.0 ? HeaterStatus.ON : HeaterStatus.OFF
        );
    }
}
