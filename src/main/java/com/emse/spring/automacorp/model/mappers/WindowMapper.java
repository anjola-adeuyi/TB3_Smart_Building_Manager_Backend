package com.emse.spring.automacorp.model.mappers;

import com.emse.spring.automacorp.model.WindowStatus;
import com.emse.spring.automacorp.model.entities.WindowEntity;
import com.emse.spring.automacorp.model.records.dao.Window;

public class WindowMapper {
    public static Window of(WindowEntity window) {
        return new Window(
                window.getId(),
                window.getName(),
                SensorMapper.of(window.getWindowStatus()).value() == 1.0 ? WindowStatus.OPENED : WindowStatus.CLOSED,
                window.getRoom().getId()
        );
    }
}
