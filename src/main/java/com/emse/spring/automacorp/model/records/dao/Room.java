package com.emse.spring.automacorp.model.records.dao;

import java.util.List;

public record Room(Long id, String name, Double currentTemp, Double targetTemp, int floor, Long buildingId, List<Window> windows, List<Heater> heaters) {
}
