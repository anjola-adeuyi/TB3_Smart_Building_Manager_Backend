package com.emse.spring.automacorp.model.mappers;

import com.emse.spring.automacorp.model.entities.BuildingEntity;
import com.emse.spring.automacorp.model.records.dao.Building;

import java.util.stream.Collectors;

public class BuildingMapper {
    public static Building of(BuildingEntity building) {
        return new Building(
                building.getId(),
                building.getName(),
                building.getOutsideTemperature().getValue(),
                building.getRooms().stream().map(RoomMapper::of).collect(Collectors.toList())
        );
    }
}
