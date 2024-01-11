package com.emse.spring.automacorp.model.records.dao;

import com.emse.spring.automacorp.model.SensorType;

public record Sensor(Long id, String name, Double value, SensorType sensorType) {
}
