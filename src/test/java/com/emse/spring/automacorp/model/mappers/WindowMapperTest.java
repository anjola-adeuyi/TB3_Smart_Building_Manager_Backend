package com.emse.spring.automacorp.model.mappers;

import com.emse.spring.automacorp.model.WindowStatus;
import com.emse.spring.automacorp.model.entities.RoomEntity;
import com.emse.spring.automacorp.model.entities.SensorEntity;
import com.emse.spring.automacorp.model.SensorType;
import com.emse.spring.automacorp.model.entities.WindowEntity;
import com.emse.spring.automacorp.model.records.dao.Window;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class WindowMapperTest {
    @Test
    void shouldMapWindow() {
        // Arrange
        SensorEntity currentTemperature = new SensorEntity(SensorType.TEMPERATURE, "Room temperature");
        currentTemperature.setId(1L);
        currentTemperature.setValue(24.2);

        RoomEntity roomEntity = new RoomEntity("Room", currentTemperature, 1);
        roomEntity.setId(1L);
        roomEntity.setTargetTemp(22.0);

        SensorEntity windowStatus = new SensorEntity(SensorType.STATUS, "Window status");
        windowStatus.setId(2L);
        windowStatus.setValue(0.0);

        WindowEntity windowEntity = new WindowEntity("Window 1", windowStatus, roomEntity);
        windowEntity.setId(2L);
        roomEntity.setWindows(List.of(windowEntity));

        // Act
        Window window = WindowMapper.of(windowEntity);

        //Assert
        Window expectedWindow = new Window (
                2L,
                "Window 1",
                WindowStatus.CLOSED,
                1L
        );
        Assertions.assertThat(window).usingRecursiveAssertion().isEqualTo(expectedWindow);
    }
}