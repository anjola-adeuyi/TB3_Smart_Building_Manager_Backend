package com.emse.spring.automacorp.model.mappers;

import com.emse.spring.automacorp.model.HeaterStatus;
import com.emse.spring.automacorp.model.SensorType;
import com.emse.spring.automacorp.model.entities.HeaterEntity;
import com.emse.spring.automacorp.model.entities.WindowEntity;
import com.emse.spring.automacorp.model.records.dao.Heater;
import com.emse.spring.automacorp.model.entities.RoomEntity;
import com.emse.spring.automacorp.model.entities.SensorEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class HeaterMapperTest {

    @Test
    void shouldMapHeater() {
        //Arrange
        SensorEntity currentTemperature = new SensorEntity(SensorType.TEMPERATURE, "Room temperature");
        currentTemperature.setId(3L);
        currentTemperature.setValue(24.2);

        SensorEntity windowStatus = new SensorEntity(SensorType.STATUS, "Window Status");
        windowStatus.setId(4L);
        windowStatus.setValue(0.0);

        SensorEntity heaterStatus = new SensorEntity(SensorType.STATUS, "Heater Status");
        heaterStatus.setId(5L);
        heaterStatus.setValue(1.0);

        RoomEntity roomEntity = new RoomEntity("Room", currentTemperature, 1);
        roomEntity.setId(1L);
        roomEntity.setTargetTemp(22.0);

        WindowEntity window = new WindowEntity("Window 1", windowStatus, roomEntity);
        window.setId(2L);
        roomEntity.setWindows(List.of(window));
        roomEntity.setHeaters(List.of());

        HeaterEntity heaterEntity = new HeaterEntity();
        heaterEntity.setId(2L);
        heaterEntity.setName("Heater Test");
        heaterEntity.setRoom(roomEntity);
        heaterEntity.setStatus(heaterStatus);

        //Act
        Heater heater = HeaterMapper.of(heaterEntity);

        //Assert
        Heater expectedHeater = new Heater(
                2L,
                "Heater Test",
                1L,
                HeaterStatus.ON
        );

        Assertions.assertThat(heater).usingRecursiveAssertion().isEqualTo(expectedHeater);
    }
}