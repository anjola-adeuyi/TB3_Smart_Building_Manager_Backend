package com.emse.spring.automacorp.model.records.dao;

import com.emse.spring.automacorp.model.HeaterStatus;

public record Heater(Long id, String name, Long roomId, HeaterStatus heaterStatus) {
}
