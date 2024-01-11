package com.emse.spring.automacorp.model.records.dto;

import com.emse.spring.automacorp.model.SensorType;

public record SensorCommand(String name, Double value, SensorType sensorType) {
}
